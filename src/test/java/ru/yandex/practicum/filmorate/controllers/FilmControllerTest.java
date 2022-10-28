package ru.yandex.practicum.filmorate.controllers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;

class FilmControllerTest {

    Film testFilm1 = new Film(0, "Test film one", "This is the test film number one",
            "2000-01-01", 200);
    Film testFilm2 = new Film(0, "", "This is the test film without name",
            "2000-01-01", 200);
    Film testFilm3 = new Film(0, "Test film three", "This is the test film with invalid description               " +
            "                                                                                                       " +
            "                                                                                                        ",
            "2000-01-01", 200);
    Film testFilm4 = new Film(0, "Test film four", "This is the test film with invalid date of release",
            "1895-11-10", 200);
    Film testFilm5 = new Film(0, "Test film five", "This is the test film with invalid duration",
            "2000-01-01", -1);


    FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
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
            Assertions.assertEquals("Invalid name", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDescription() {                           //Должен выбрасывать ошибку "Неправильное описание"
        try {
            controller.addFilm(testFilm3);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDate() {                                //Должен выбрасывать ошибку "Неправильная дата выхода"
        try {
            controller.addFilm(testFilm4);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid date of release", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidDuration() {                      //Должен выбрасывать ошибку "Неправильная продолжительность"
        try {
            controller.addFilm(testFilm5);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid duration", e.getMessage());
        }
    }

    @Test
    void shouldThrowNotAdded() {                                     //Должен выбрасывать ошибку "Фильм не был добавлен"
        try {
            controller.updateFilm(testFilm1);
        } catch (ValidationException e) {
            Assertions.assertEquals("The film hasn't been added", e.getMessage());
        }
    }
}