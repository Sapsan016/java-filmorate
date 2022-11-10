package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmController {

    final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public ArrayList<Film> getAllFilms() {                                     //Запрос на получение списка всех фильмов
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")                                                           //запрос на получение фильма по Id
    public Film getFilmById(@PathVariable("id") int id){
        return filmService.getFilmById(id);
    }
    @PostMapping()
    public Film addFilm(@RequestBody Film film) {                                         //Запрос на добавление фильма
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {            //Запрос на обновление фильма
        return filmService.updateFilm(film);
    }
    @DeleteMapping("/{id}")                                                               //запрос на удаление фильма
    public void removeFilmById(@PathVariable("id") int id){
        filmService.removeFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")                                                  //Запрос на добавление лайка
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId){
        filmService.addLike(filmId,userId);
    }
    @DeleteMapping("/{id}/like/{userId}")                                                 //Запрос на удаление лайка
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId,userId);
    }
    @GetMapping("/popular")                                            //Запрос на получение самых популярных фильмов
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.getMostPopularFilms(count);
    }
}