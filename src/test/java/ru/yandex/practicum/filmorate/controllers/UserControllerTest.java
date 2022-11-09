package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class UserControllerTest {

    UserController controller;
    InMemoryUserStorage inMemoryUserStorage;

    User testUser1 = new User(0, "user1@yandex.ru", "User number one",
            "user1", LocalDate.of(1984, 2, 14),new HashSet<>());
    User testUser2 = new User(0, "", "This is the test user without email",
            "user2", LocalDate.of(1984, 2, 14), new HashSet<>());
    User testUser3 = new User(0, "user3@yandex.ru", "This is the test user with invalid login",
            " ", LocalDate.of(1984, 2, 14), new HashSet<>());
    User testUser4 = new User(0, "user4@yandex.ru", "", "user4",
            LocalDate.of(1984, 2, 14), new HashSet<>());
    User testUser5 = new User(0, "user5@yandex.ru", "This is the test user with invalid birthday",
            "user5", LocalDate.now().plusDays(1), new HashSet<>());

    User testUser6 = new User(0, "user6@yandex.ru", "User number six",
            "user6", LocalDate.of(2000, 2, 14),new HashSet<>());

    @BeforeEach
    void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        controller =  new UserController(new UserService(inMemoryUserStorage));
    }

    @Test
    void shouldCreateAndReturnUser() {                                  //Должен добавляться и возвращаться пользователь
        controller.createUser(testUser1);
        ArrayList<User> testList = new ArrayList<>();
        testList.add(testUser1);
        Assertions.assertEquals(testList, controller.getAllUsers());
    }
    @Test
    void shouldReturnUserById(){                                 //Должен добавляться и возвращаться пользователь по Id
        controller.createUser(testUser1);
        Assertions.assertEquals(testUser1,controller.getUserById(testUser1.getId()));
        System.out.println( controller.getUserById(testUser1.getId()));

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
    void shouldThrowNotCreated() {                              //Должен выбрасывать ошибку "Пользователь не найден"
        try {
            controller.updateUser(testUser1);
        } catch (UserNotFoundException e) {
            Assertions.assertEquals("The user with Id= 0 was not found", e.getMessage());
        }
    }
    @Test
    void shouldAddFriend(){
        controller.createUser(testUser1);
        controller.createUser(testUser6);
        controller.addFriend(testUser1.getId(),testUser6.getId());
        User user = controller.getUserById(testUser1.getId());
        Set<Integer> checkList = new HashSet<>();
        checkList.add(testUser6.getId());
        Assertions.assertEquals(user.getFriendsIds(),checkList);

    }
}