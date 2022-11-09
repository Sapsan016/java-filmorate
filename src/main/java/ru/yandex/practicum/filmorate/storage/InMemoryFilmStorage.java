package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
        if(!films.containsKey(id)){                                                           //проверяем наличие фильма
            log.error("Film not found");
            throw new FilmNotFoundException("Film with Id" + id + "not found");
        }
        return films.get(id);
    }

    @Override
    public ArrayList<Film> getAllFilms() {                                               //Получаем список всех фильмов
        return new ArrayList<>(films.values());
    }
    @Override
    public Film addFilm(@RequestBody Film film) {                                                    //Добавляем фильм
        if(validateFilm(film)) {                                               //Если фильм прошел валидацию добавляем
            film.setId(getGeneratedId());                                         //Добавляем Id и пустой список лайков
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("The film with Id= " + film.getId() + " was added");
            return film;
        } else {
            log.error("Validation failed");
            throw new ValidationException("Validation failed");
        }
    }
    @Override
    public Film updateFilm(@RequestBody Film film) throws ValidationException {                        //Обновляем фильм
        if (!films.containsKey(film.getId())) {
            log.error("The film was not found");
            throw new FilmNotFoundException("The film with Id= " + film.getId() + " was not found");    //Проверяем создание фильма
        }
        if(validateFilm(film)) {                                                 //Если фильм прошел валидацию обновляем
            Set<Integer> temp = films.get(film.getId()).getLikes();          //Сохраняем список лайков перед обновлением
            film.setLikes(temp);
            films.put(film.getId(),film);
            log.info("The film with Id= " + film.getId() + " was updated");
            return film;
        } else {
            log.error("Validation failed");
            throw new ValidationException("Validation failed");
        }
    }
    @Override
    public void removeFilmById(int id) {
        if (!films.containsKey(id)) {
            log.error("The film was not found");
            throw new FilmNotFoundException("The film with Id= " + id + "not found");    //Проверяем создание фильма
        }
        films.remove(id);
        log.info("The film with Id= " + id + " was removed");
    }

    @Override
    public boolean validateFilm(Film film) {
        LocalDate firstFilm = LocalDate.of(1895,12,28);                       //День рождения кино

        if (film.getName() == null || film.getName().isBlank()) {                            //Валидация названия фильма
            log.error("Invalid name");
            throw new ValidationException("Invalid name");
        }
        if (film.getDescription().length() > 200) {                                                 //Валидация описания
            log.error("Invalid description");
            throw new ValidationException("Invalid description");
        }
        if (film.getReleaseDate().isBefore(firstFilm)) {                                  //Валидация даты выхода фильма
            log.error("Invalid name date of release");
            throw new ValidationException("Invalid date of release");
        }
        if (film.getDuration() < 0) {                                               //Валидация продолжительности фильма
            log.error("Invalid duration");
            throw new ValidationException("Invalid duration");
        }
        return true;
    }
}