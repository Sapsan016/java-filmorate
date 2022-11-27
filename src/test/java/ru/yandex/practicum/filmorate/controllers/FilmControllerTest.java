package ru.yandex.practicum.filmorate.controllers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

class FilmControllerTest {

    Film testFilm1 = new Film(0, "Test film one", "This is the test film number one",
            LocalDate.of(2000,1,1), 200, new HashSet<>(), "G", new ArrayList<>());
    Film testFilm2 = new Film(0, "", "This is the test film without name",
            LocalDate.of(2000,1,1), 200, new HashSet<>(), "G", new ArrayList<>());
    Film testFilm3 = new Film(0, "Test film three", "This is the test film with invalid description               " +
            "                                                                                                       " +
            "                                                                                                        ",
            LocalDate.of(2000,1,1), 200, new HashSet<>(), "G", new ArrayList<>());
    Film testFilm4 = new Film(0, "Test film four", "This is the test film with invalid date of release",
            LocalDate.of(1895,11,10), 200, new HashSet<>(), "G", new ArrayList<>());
    Film testFilm5 = new Film(0, "Test film five", "This is the test film with invalid duration",
            LocalDate.of(2000,1,1), -1, new HashSet<>(), "G", new ArrayList<>());


    FilmController controller;
    FilmStorage inMemoryFilmStorage;

    @BeforeEach
    void setUp() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        controller = new FilmController(new FilmService(inMemoryFilmStorage));
    }

    @Test
    void shouldAddAndReturnFilm() {                                           //Должен добавляться и возвращаться фильм
        controller.addFilm(testFilm1);
        ArrayList<Film> testList = new ArrayList<>();
        testList.add(testFilm1);
        Assertions.assertEquals(testList, controller.getAllFilms());
    }

    @Test
    void updateFilm() {                                                                       //Должен обновиться фильм
        controller.addFilm(testFilm1);
        testFilm1.setName("Updated name");
        Assertions.assertEquals(testFilm1, controller.updateFilm(testFilm1));
    }

    @Test
    void shouldThrowInvalidName() {                                        //Должен выбрасывать ошибку "Неправильное имя"
        try {
            controller.addFilm(testFilm2);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверное имя", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDescription() {                           //Должен выбрасывать ошибку "Неправильное описание"
        try {
            controller.addFilm(testFilm3);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверное описание", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDate() {                                //Должен выбрасывать ошибку "Неправильная дата выхода"
        try {
            controller.addFilm(testFilm4);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверная дата выхода", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDuration() {                      //Должен выбрасывать ошибку "Неправильная продолжительность"
        try {
            controller.addFilm(testFilm5);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверная продолжительность", e.getMessage());
        }
    }

    @Test
    void shouldThrowNotAdded() {                                    //Должен выбрасывать ошибку "Фильм не найден"
        try {
            controller.updateFilm(testFilm1);
        } catch (FilmNotFoundException e) {
            Assertions.assertEquals("Фильм с Id= 0 не найден", e.getMessage());
        }
    }
}