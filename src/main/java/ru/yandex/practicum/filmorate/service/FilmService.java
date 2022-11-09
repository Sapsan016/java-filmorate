package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

public class FilmService {
    final FilmStorage filmStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        }

    public void addLike(int filmId, int userId){                                                      //Добавление лайка
        Film film = filmStorage.getFilmById(filmId);
        if(film == null || userId == 0){
            throw new FilmNotFoundException("Film not found");
        }
        film.getLikes().add(userId);
    }

    public void  removeLike(int filmId, int userId) {                                                   //Удаление лайка
        Film film = filmStorage.getFilmById(filmId);
        if(film == null || userId == 0) {
            throw new FilmNotFoundException("Film not found");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopularFilms(int count) {                               //вывод наиболее популярных фильмов
        List<Film> films = filmStorage.getAllFilms();
        return films.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size())) //сортируем фильмы по количеству лайков
                .sorted(Collections.reverseOrder())                                      //делаем сортировку по убыванию
                .limit(count)
                .collect(Collectors.toList());
    }
}
