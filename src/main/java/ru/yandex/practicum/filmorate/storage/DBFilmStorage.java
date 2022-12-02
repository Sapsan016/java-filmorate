package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)

public class DBFilmStorage implements FilmStorage{

    JdbcTemplate jdbcTemplate;

    @Autowired
    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film getFilmById(int id) {                                                           //Возвращаем фильм по Id
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING " +
                "from FILMS where FILM_ID = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

        if (film == null) {                                                     //проверяем наличие пользовтеля
            log.error("Фильм не найден");
            throw new UserNotFoundException("Фильм с Id" + id + " не найден");
        }

        return film;
    }

    @Override
    public List<Film> getAllFilms() {                                               //Получаем список всех фильмов
        String sqlQuery = "select FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }
    @Override
    public Film addFilm(@RequestBody Film film) {                                                    //Добавляем фильм
        if(validateFilm(film)) {                                               //Если фильм прошел валидацию добавляем
            String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING) " +
                    "values (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();               // Переменная для получениЯ и хранения id
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4,film.getDuration());
                stmt.setInt(5, film.getRating());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().intValue());                        //Задаем фильму сгенерированый БД id
            log.info("Фильм с Id = " + film.getId() + " создан");

        } else {
            log.error("Не прошла валидация");
            throw new ValidationException("Не прошла валидация");
        }
        film.setLikes(new HashSet<>());
        return film;
    }
    @Override
    public Film updateFilm(@RequestBody Film film) throws ValidationException {                        //Обновляем фильм
//        if (!films.containsKey(film.getId())) {
//            log.error("Фильм не найден");
//            throw new FilmNotFoundException("Фильм с Id= " + film.getId() + " не найден");    //Проверяем создание фильма
//        }
        if (validateFilm(film)) {                                                 //Если фильм прошел валидацию обновляем
           String sqlQuery = "update FILMS set " +
                        " FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING = ?" +
                        "where FILM_ID = ?";
                jdbcTemplate.update(sqlQuery
                        , film.getName()
                        , film.getDescription()
                        , film.getReleaseDate()
                        , film.getDuration()
                        , film.getRating());
                log.info("Фильм с Id= " + film.getId() + " обновлен");
                return film;
            } else {
                log.error("Валидация не прошла");
                throw new ValidationException("Валидация не прошла");
            }
        }

    @Override
    public void removeFilmById(int id) {
//        if (!films.containsKey(id)) {
//            log.error("Фильм не найден");
//            throw new FilmNotFoundException("Фильм с Id= " + id + " не найден");    //Проверяем создание фильма
//        }
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);

        log.info("Фильм с Id= " + id + " удален");
    }

    @Override
    public boolean validateFilm(Film film) {
        LocalDate firstFilm = LocalDate.of(1895,12,28);                       //День рождения кино

        if (film.getName() == null || film.getName().isBlank()) {                            //Валидация названия фильма
            log.error("Неверное имя");
            throw new ValidationException("Неверное имя");
        }
        if (film.getDescription().length() > 200) {                                                 //Валидация описания
            log.error("Неверное описание");
            throw new ValidationException("Неверное описание");
        }
        if (film.getReleaseDate().isBefore(firstFilm)) {                                  //Валидация даты выхода фильма
            log.error("Неверная дата выхода");
            throw new ValidationException("Неверная дата выхода");
        }
        if (film.getDuration() < 0) {                                               //Валидация продолжительности фильма
            log.error("Неверная продолжительность");
            throw new ValidationException("Неверная продолжительность");
        }
        return true;
    }
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException { // Метод для преобразования запроса
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .rating(resultSet.getInt("RATING"))
                .build();
    }
}