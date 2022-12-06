package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public interface GenreStorage {
    List<Genre> getAllGenres();
    Genre getGenreById(int id);

    static Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {            // Преобразуем запрос в жанр
        final int id = rs.getInt("GENRE_ID");
        final String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }
}
