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
    public List<Film> getAllFilms() {                                     //Эндпойнт для получение списка всех фильмов
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")                                                           //Эндпойнт для получение фильма по Id
    public Film getFilmById(@PathVariable("id") int id){
        return filmService.getFilmById(id);
    }
    @PostMapping()
    public void addFilm(@RequestBody Film film) {                                         //Эндпойнт для добавление фильма
        filmService.addFilm(film);
    }

    @PutMapping()
    public void updateFilm(@RequestBody Film film) throws ValidationException {            //Эндпойнт для обновление фильма
        filmService.updateFilm(film);
    }
    @DeleteMapping("/{id}")                                                               //Эндпойнт для удаление фильма
    public void removeFilmById(@PathVariable("id") int id){
        filmService.removeFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")                                                  //Эндпойнт для добавление лайка
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId){
        filmService.addLike(filmId,userId);
    }
    @DeleteMapping("/{id}/like/{userId}")                                                 //Эндпойнт для удаление лайка
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId,userId);
    }
    @GetMapping("/popular")                                            //Эндпойнт для самых популярных фильмов
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.getMostPopularFilms(count);
    }
}