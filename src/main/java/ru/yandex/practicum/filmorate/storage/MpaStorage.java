package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public interface MpaStorage {

    List<MPA> getAllMpa();

    MPA getMpaById(int id);

    static MPA mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {                  // Преобразуем запрос в МРА
        final int id = rs.getInt("MPA_ID");
        final String name = rs.getString("MPA_NAME");
        return new MPA(id, name);
    }
}
