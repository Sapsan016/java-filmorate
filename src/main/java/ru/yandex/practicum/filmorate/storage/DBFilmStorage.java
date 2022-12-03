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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class DBFilmStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film getFilmById(int id) {                                                           //Возвращаем фильм по Id
        if (isPresent(id)) {
            String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING " +
                    "from FILMS where FILM_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        }
        throw new FilmNotFoundException("Фильм не найден");
    }

    @Override
    public List<Film> getAllFilms() {                                               //Получаем список всех фильмов
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(@RequestBody Film film) {                                                    //Добавляем фильм
        if (validateFilm(film)) {                                               //Если фильм прошел валидацию добавляем
            String filmQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING) " +
                    "values (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();               // Переменная для получениЯ и хранения id
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(filmQuery, new String[]{"FILM_ID"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().intValue());                        //Задаем фильму сгенерированый БД id
            if (film.getGenres() != null) {
                final String genreQuery = "INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
                for (Genre g : film.getGenres()) {
                    jdbcTemplate.update(genreQuery, film.getId(), g.getId());
                }
            }
            log.info("Фильм с Id = " + film.getId() + " создан");
        } else {
            log.error("Не прошла валидация");
            throw new ValidationException("Не прошла валидация");
        }
        film.setMpa(getMpaFromBD(film.getId()));                    //Задаем фильму МРА и список жанров сохраненные в БД
        film.setGenres(getGenresFromBD(film.getId()));
        film.setLikes(new HashSet<>());                                                  //Добавляем новый список лайков
        return film;
    }

    @Override
    public Film updateFilm(@RequestBody Film film) throws ValidationException {                        //Обновляем фильм
        if (validateFilm(film) && isPresent(film.getId())) {       //Если фильм прошел валидацию и есть в базе обновляем
            Set<Integer> temp = film.getLikes();
            String sqlQuery = "update FILMS set " +
                    " FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING = ?" +
                    "where FILM_ID = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId());
            if (film.getGenres() != null) {
                final String genreQuery = "update  FILMS_GENRE set GENRE_ID = ? WHERE FILM_ID = ?";
                for (Genre g : film.getGenres()) {
                    jdbcTemplate.update(genreQuery, g.getId(), film.getId());
                }
            }
            log.info("Фильм с Id= " + film.getId() + " обновлен");
            film.setLikes(temp);
            film.setMpa(getMpaFromBD(film.getId()));                    //Задаем фильму МРА и список жанров сохраненные в БД
            film.setGenres(getGenresFromBD(film.getId()));
            return film;
        } else {
            log.error("Валидация не прошла");
            throw new ValidationException("Валидация не прошла");
        }
    }

    @Override
    public void removeFilmById(int id) {                                                          //Удаляем фильм из БД
        if (!isPresent(id)) {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм с Id= " + id + " не найден");
        }
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Фильм с Id= " + id + " удален");
    }

    @Override
    public boolean validateFilm(Film film) {
        LocalDate firstFilm = LocalDate.of(1895, 12, 28);                     //День рождения кино

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

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {      // Преобразуем запрос в фильм
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(mapRowToMpa(resultSet, rowNum))
                .build();
    }

    private MPA mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {                  // Преобразуем запрос в МРА
        final int id = rs.getInt("MPA_ID");
        final String name = rs.getString("MPA_NAME");
        return new MPA(id, name);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {            // Преобразуем запрос в жанр
        final int id = rs.getInt("GENRE_ID");
        final String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    private boolean isPresent(int id) {                                               //Проверяем наличие фильма в базе
        final String check = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(check, id);
        if (!filmRows.next()) {
            log.warn("Фильм с id {} не найден", id);
            throw new FilmNotFoundException("Фильм не найден");
        }
        return true;
    }

    private List<Genre> getGenresFromBD(int id) {                                        //Получаем список жанров из БД
        final String genreSQL = "SELECT GENRES.GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "JOIN FILMS_GENRE FG on GENRES.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(genreSQL, this::mapRowToGenre, id);
    }

    private MPA getMpaFromBD(int id) {                                                             //Получаем МРА из БД
        final String MPASQL = "SELECT MPA_ID, MPA_NAME " +
                "FROM MPA " +
                "JOIN FILMS F ON MPA.MPA_ID = F.RATING " +
                "WHERE film_id = ?";
        return jdbcTemplate.queryForObject(MPASQL, this::mapRowToMpa, id);
    }
}