package ru.yandex.practicum.filmorate.service;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)

public class GenreService {

    GenreStorage genreStorage;
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAll() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(@PathVariable("id")int id) {
        return genreStorage.getGenreById(id);
    }




}
