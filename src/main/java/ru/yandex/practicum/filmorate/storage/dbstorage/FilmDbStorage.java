package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Qualifier("filmStorage")
@Primary

public class FilmDbStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate) //Добавляем фильм в таблицу FILMS
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("id");
        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue(); //и сохраняем id

        for (String genre : film.getGenres()) {                  //Добавляем в таблицу FILMS_GENRE список жанров фильма
            String sqlQuery = "insert into FILMS_GENRE(GENRE_ID, FILM_ID) values (?, ?)";
            jdbcTemplate.update(sqlQuery, genre, filmId);
        }
        if(!film.getLikes().isEmpty()) {            //Если список лайков фильма не пустой
            for (Integer like : film.getLikes()) {  //Добавляем в таблицу FILM_LIKES список лайков фильма
                String sqlQuery = "insert into FILM_LIKES(FILM_ID, USER_ID) values (?, ?)";
                jdbcTemplate.update(sqlQuery, filmId, like);
            }
        }
    }

    @Override
    public Film getFilmById(int id) {                                                               //Получаем фильм
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING " +
                "from FILMS where FILM_ID = ?";
        Film film =  jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);  //Преобразовываем результат запроса
                                                                                     //в объект
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select GENRE_NAME " + // Получаем список жанров
                "from GENRES right join FILMS_GENRE " +
                "on GENRES.GENRE_ID = FILMS_GENRE.GENRE_ID right join FILMS " +
                "on FILMS_GENRE.FILM_ID = FILMS.FILM_ID where FILMS.FILM_ID = ?");

        List<String> genres = new ArrayList<>();
        if(genreRows.next()) {
            genres.add(
                    genreRows.getString("GENRE_NAME"));
        }
        film.setGenres(genres);
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select USER_ID " +         //Получаем список лайков
                "from FILM_LIKES right join FILMS " +
                "on FILM_LIKES.FILM_ID = FILMS.FILM_ID where FILMS.FILM_ID = ?");

        Set<Integer> likes = new HashSet<>();
        if(likesRows.next()) {
            likes.add(
                    likesRows.getInt("USER_ID"));
        }
        film.setLikes(likes);
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException { // Метод для преобразования запроса
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .rating(resultSet.getString("RATING"))
                .build();
    }
    private Film mapRowToGenres(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .rating(resultSet.getString("RATING"))
                .build();
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING from FILMS";
        return (ArrayList<Film>) jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }


    @Override
    public void updateFilm(Film film) throws ValidationException {
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?,  DURATION = ?, RATING = ?" +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                , jdbcTemplate.update(sqlQuery,
                        film.getName(),
                        film.getDescription(),
                        film.getDuration(),
                        film.getRating()));
    }

    @Override
    public boolean validateFilm(Film film) {
        return false;
    }

    @Override
    public void removeFilmById(int id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);

    }
}
