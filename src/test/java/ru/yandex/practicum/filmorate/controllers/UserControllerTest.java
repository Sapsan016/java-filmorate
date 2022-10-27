package ru.yandex.practicum.filmorate.controllers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class UserControllerTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    UserController controller;

    User testUser1 = new User(0, "user1@yandex.ru", "User number one",
            "user1", "1984-02-14");
    User testUser2 = new User(0, "", "This is the test user without email",
            "user2", "1984-02-14");
    User testUser3 = new User(0, "user3@yandex.ru", "This is the test user with invalid login",
            " ", "1984-02-14");
    User testUser4 = new User(0, "user4@yandex.ru", "", "user4",
            "1984-02-14");
    User testUser5 = new User(0, "user5@yandex.ru", "This is the test user with invalid birthday",
            "user5", LocalDateTime.now().plusDays(1).format(formatter));


    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void shouldCreateAndReturnUser() {                                  //Должен добавляться и возвращаться пользователь
        controller.createUser(testUser1);
        ArrayList<User> testList = new ArrayList<>();
        testList.add(testUser1);
        Assertions.assertEquals(testList, controller.getAllUsers());
    }

    @Test
    void updateUser() {                                                                 //Должен обновиться пользователь
        controller.createUser(testUser1);
        testUser1.setName("Updated name");
        Assertions.assertEquals(testUser1, controller.updateUser(testUser1));
        }

    @Test
    void shouldThrowInvalidEmail() {                                    //Должен выбрасывать ошибку "Неправильный email"
        try {
            controller.createUser(testUser2);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid email", e.getMessage());
        }
    }

    @Test
    void shouldThrowInvalidLogin() {                                    //Должен выбрасывать ошибку "Неправильный логин"
        try {
            controller.createUser(testUser3);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid login", e.getMessage());
        }
    }

    @Test
    void shouldUseLoginAsName() {                                              //Должен использовать логин вместо имени"
        controller.createUser(testUser4);
        Assertions.assertEquals("user4", testUser4.getName());
    }

    @Test
    void shouldThrowInvalidDateOfBirth() {                      //Должен выбрасывать ошибку "Неправильная дата рождения"
        try {
            controller.createUser(testUser5);
        } catch (ValidationException e) {
            Assertions.assertEquals("Invalid birthday", e.getMessage());
        }
    }

    @Test
    void shouldThrowNotCreated() {                              //Должен выбрасывать ошибку "Пользователь не был создан"
        try {
            controller.updateUser(testUser1);
        } catch (ValidationException e) {
            Assertions.assertEquals("The user hasn't been created", e.getMessage());
        }
    }
}