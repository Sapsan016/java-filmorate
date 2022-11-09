package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)

public class InMemoryFilmStorage implements FilmStorage{
    final HashMap<Integer, Film> films = new HashMap<>();
    int generatedId = 0;

    @Override
    public int getGeneratedId() {
        return ++generatedId;
    }

    @Override
    public Film getFilmById(int id) {                                                           //Возвращаем фильм по Id
        return films.get(id);
    }

    @Override
    public ArrayList<Film> getAllFilms() {                                               //Получаем список всех фильмов
        return new ArrayList<>(films.values());
    }
    @Override
    public Film addFilm(@RequestBody Film film) {                                                    //Добавляем фильм
        if(validateFilm(film)) {                                          //Если пользователь прошел валидацию добавляем
            film.setId(getGeneratedId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Validation failed");
        }
    }
    @Override
    public Film updateFilm(@RequestBody Film film) throws ValidationException {              //Обновляем фильм
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

    @Override
    public boolean validateFilm(Film film) {
        LocalDate firstFilm = LocalDate.of(1895,12,28);                       //День рождения кино

        if (film.getName() == null || film.getName().isBlank()) {                            //Валидация названия фильма
            throw new ValidationException("Invalid name");
        }
        if (film.getDescription().length() > 200) {                                                 //Валидация описания
            throw new ValidationException("Invalid description");
        }
        if (film.getReleaseDate().isBefore(firstFilm)) {                                  //Валидация даты выхода фильма
            throw new ValidationException("Invalid date of release");
        }
        if (film.getDuration() < 0) {                                               //Валидация продолжительности фильма
            throw new ValidationException("Invalid duration");
        }
        return true;
    }
}
