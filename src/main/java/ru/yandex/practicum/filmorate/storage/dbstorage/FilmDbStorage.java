package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.ArrayList;

@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)



public class FilmDbStorage implements FilmStorage {
    @Override
    public int getGeneratedId() {
        return 0;
    }
    @Override
    public Film addFilm(Film film) {
        return null;
    }



    @Override
    public Film getFilmById(int id) {
        return null;
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
