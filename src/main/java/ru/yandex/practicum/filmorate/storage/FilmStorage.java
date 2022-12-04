package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Component
public interface FilmStorage {

    Film getFilmById(int id);

    List<Film> getAllFilms();                                                      //Получаем список всех фильмов

    Film addFilm(@RequestBody Film film);                                                     //Добавляем фильм

    Film updateFilm(@RequestBody Film film) throws ValidationException;                   //Обновляем фильм
    public Film addLike(int filmId, int userId);

    boolean validateFilm(Film film);

    void removeFilmById(int id);
}
