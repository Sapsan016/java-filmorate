package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
//@Primary
@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)

public class InMemoryFilmStorage implements FilmStorage{
    HashMap<Integer, Film> films = new HashMap<>();
    @NonFinal
    int generatedId = 0;

    @Override
    public int getGeneratedId() {
        return ++generatedId;
    }

    @Override
    public Film getFilmById(int id) {                                                           //Возвращаем фильм по Id
        if(!films.containsKey(id)){                                                           //проверяем наличие фильма
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм с Id=" + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public ArrayList<Film> getAllFilms() {                                               //Получаем список всех фильмов
        return new ArrayList<>(films.values());
    }
    @Override
    public void addFilm(@RequestBody Film film) {                                                    //Добавляем фильм
        if(validateFilm(film)) {                                               //Если фильм прошел валидацию добавляем
            film.setId(getGeneratedId());                                         //Добавляем Id и пустой список лайков
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("Фильм с Id= " + film.getId() + " не найден");
            //return film;
        } else {
            log.error("Не прошла валидация");
            throw new ValidationException("Не прошла валидация");
        }
    }
    @Override
    public void updateFilm(@RequestBody Film film) throws ValidationException {                        //Обновляем фильм
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм с Id= " + film.getId() + " не найден");    //Проверяем создание фильма
        }
        if(validateFilm(film)) {                                                 //Если фильм прошел валидацию обновляем
            Set<Integer> temp = films.get(film.getId()).getLikes();          //Сохраняем список лайков перед обновлением
            film.setLikes(temp);
            films.put(film.getId(),film);
            log.info("Фильм с Id= " + film.getId() + " обновлен");

        } else {
            log.error("Не прошла валидация");
            throw new ValidationException("Не прошла валидация");
        }
    }
    @Override
    public void removeFilmById(int id) {
        if (!films.containsKey(id)) {
            log.error("Фильм не найден");
            throw new FilmNotFoundException("Фильм с Id= " + id + " не найден");    //Проверяем создание фильма
        }
        films.remove(id);
        log.info("Фильм с Id= " + id + " удален");
    }

    @Override
    public boolean validateFilm(Film film) {
        LocalDate firstFilm = LocalDate.of(1895,12,28);                       //День рождения кино

        if (film.getName() == null || film.getName().isBlank()) {                            //Валидация названия фильма
            log.error("Неверное имя");
            throw new ValidationException("Неверное имя");
        }
        if (film.getDescription().length() > 200) {                                                 //Валидация описания
            log.error("Неверное описание");
            throw new ValidationException("Неверное описание");
        }
        if (film.getReleaseDate().isBefore(firstFilm)) {                                  //Валидация даты выхода фильма
            log.error("Неверная дата выхода");
            throw new ValidationException("Неверная дата выхода");
        }
        if (film.getDuration() < 0) {                                               //Валидация продолжительности фильма
            log.error("Неверная продолжительность");
            throw new ValidationException("Неверная продолжительность");
        }
        return true;
    }
}