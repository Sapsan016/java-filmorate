package ru.yandex.practicum.filmorate.storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
@Slf4j
@Repository
public class DBGenreStorage implements GenreStorage{
    JdbcTemplate jdbcTemplate;
    @Autowired
    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, GenreStorage::mapRowToGenre);
    }
    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            log.warn("Genre с id {} не найден", id);
            throw new GenreNotFoundException("Genre не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, GenreStorage::mapRowToGenre, id);
    }
}