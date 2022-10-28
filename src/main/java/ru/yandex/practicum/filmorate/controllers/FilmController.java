package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("films")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class FilmController {

    final HashMap<Integer, Film> films = new HashMap<>();
    int generatedId = 0;

    private int getGeneratedId(){
        return ++generatedId;
    }

    @GetMapping()
    public ArrayList<Film> getAllFilms() {                                               //Получаем список всех фильмов
        return new ArrayList<>(films.values());
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {                 //Обновляем фильм
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("The film hasn't been added");                  //Валидируем добавление фильма
        }
        if(validateFilm(film)) {                                                 //Если фильм прошел валидацию обновляем
            films.put(film.getId(),film);
            return film;
        } else {
            throw new ValidationException("Validation failed");
        }
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        if(validateFilm(film)) {                                          //Если пользователь прошел валидацию добавляем
            film.setId(getGeneratedId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Validation failed");
        }
    }
    private boolean validateFilm(Film film){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");                             //Форматтер

        LocalDate ld = LocalDate.parse("1895-12-28", formatter);                               //День рождения кино
        LocalDateTime firstFilm = LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());

        LocalDate releaseDate = LocalDate.parse(film.getReleaseDate(), formatter);
        LocalDateTime releaseDateTime = LocalDateTime.of(releaseDate, LocalDateTime.now().toLocalTime());

        if (film.getName() == null || film.getName().isBlank()) {                            //Валидация названия фильма
            throw new ValidationException("Invalid name");
        }
        if (film.getDescription().length() > 200) {                                                 //Валидация описания
            throw new ValidationException("Invalid description");
        }
        if (releaseDateTime.isBefore(firstFilm)) {                                  //Валидация даты выхода фильма
            throw new ValidationException("Invalid date of release");

        }
        if (film.getDuration() < 0) {                                               //Валидация продолжительности фильма
            throw new ValidationException("Invalid duration");
        }
        return true;
    }
}