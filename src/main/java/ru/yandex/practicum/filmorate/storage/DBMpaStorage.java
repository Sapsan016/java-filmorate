package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;

@Slf4j
@Repository
public class DBMpaStorage implements MpaStorage {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public DBMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<MPA> getAllMpa() {                                                             //Получаем все МРА из БД
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, MpaStorage::mapRowToMpa);
    }
    @Override
    public MPA getMpaById(int id) {
        String sqlQuery = "select * from MPA where MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            log.warn("MPA с id {} не найден", id);
            throw new MpaNotFoundException("MPA не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, MpaStorage::mapRowToMpa, id);
    }
}