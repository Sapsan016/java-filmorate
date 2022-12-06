package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MotionCompany;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public interface MotionCompanyStorage {
    List<MotionCompany> getAllMpa();
    MotionCompany getMpaById(int id);
    static MotionCompany mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {                  // Преобразуем запрос в МРА
        final int id = rs.getInt("MPA_ID");
        final String name = rs.getString("MPA_NAME");
        return new MotionCompany(id, name);
    }
}