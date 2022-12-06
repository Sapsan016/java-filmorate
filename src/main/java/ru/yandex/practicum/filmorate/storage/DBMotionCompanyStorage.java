package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MotionCompanyNotFoundException;
import ru.yandex.practicum.filmorate.model.MotionCompany;

import java.util.List;

@Slf4j
@Repository
public class DBMotionCompanyStorage implements MotionCompanyStorage {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public DBMotionCompanyStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<MotionCompany> getAllMpa() {                                                             //Получаем все МРА из БД
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, MotionCompanyStorage::mapRowToMpa);
    }
    @Override
    public MotionCompany getMpaById(int id) {                                                          //Получаем МРА из БД по Id
        String sqlQuery = "select * from MPA where MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            log.warn("MotionCompany с id {} не найден", id);
            throw new MotionCompanyNotFoundException("MotionCompany не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, MotionCompanyStorage::mapRowToMpa, id);
    }
}