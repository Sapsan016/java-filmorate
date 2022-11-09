package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
@Component
public interface FilmStorage {
    int getGeneratedId();

    Film getFilmById(int id);

    ArrayList<Film> getAllFilms();                                                      //Получаем список всех фильмов

    Film addFilm(@RequestBody Film film);                                                     //Добавляем фильм

    Film updateFilm(@RequestBody Film film) throws ValidationException;                   //Обновляем фильм

    boolean validateFilm(Film film);


}
