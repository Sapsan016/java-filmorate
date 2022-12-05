package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class FilmService {
    FilmStorage filmStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {                //Передаем запрос на получение списка всех фильмов в хранилище
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(@PathVariable("id") int id) {               //Передаем запрос на получение фильма в хранилище
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(@RequestBody Film film) {                      //Передаем запрос на добавление фильмав в хранилище
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@RequestBody Film film) throws ValidationException { //Передаем запрос на обновление фильма в хранилище
        return filmStorage.updateFilm(film);
    }

    public void removeFilmById(int id) {
        filmStorage.removeFilmById(id);
    }

    public void addLike(int filmId, int userId) {                                                      //Добавление лайка
        Film film = filmStorage.getFilmById(filmId);
        if (film == null || userId <= 0) {
            log.error("Фильм или пользователь не найдены");
            throw new FilmNotFoundException("Фильм или пользователь не найдены");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {                                                   //Удаление лайка
        Film film = filmStorage.getFilmById(filmId);
        if (film == null || userId <= 0) {
            log.error("Фильм или пользователь не найдены");
            throw new FilmNotFoundException("Фильм или пользователь не найдены");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int count) {                               //вывод наиболее популярных фильмов
        List<Film> films = filmStorage.getAllFilms();
        List<Film> popularFilms = films.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())//сортируем фильмы по количеству лайков
                .limit(count)
                .collect(Collectors.toList());
        log.info("Список самых популярных фильмов: " + popularFilms);
        return popularFilms;
    }
}