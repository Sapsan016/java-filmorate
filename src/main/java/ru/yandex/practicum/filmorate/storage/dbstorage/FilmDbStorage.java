package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)

public class FilmDbStorage implements FilmStorage {
     JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getGeneratedId() {
        return 0;
    }
    @Override
    public void addFilm(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION,RATING) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getRating());
    }


    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING " +
                "from FILMS where FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToEmployee, id);
    }
    private Film mapRowToEmployee(ResultSet resultSet, int rowNum) throws SQLException {
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
        return null;
    }



    @Override
    public Film updateFilm(Film film) throws ValidationException {
        return null;
    }

    @Override
    public boolean validateFilm(Film film) {
        return false;
    }

    @Override
    public void removeFilmById(int id) {

    }
}
