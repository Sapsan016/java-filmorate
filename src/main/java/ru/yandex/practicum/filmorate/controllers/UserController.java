package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ArrayList<User> getAllUsers() {                               //Запрос на получение списка всех пользователей
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")                                                     //запрос на получение пользователя по Id
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PutMapping()                                                                 //Запрос на обновление пользователя
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {                            //Запрос на создание пользователя
        return userService.createUser(user);
    }
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") int id){
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")                                           //запрос на добавление в друзья
    public void addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);

    }

    @DeleteMapping("/{id}/friends/{friendId}")                                         //запрос на удаление из друзей
    public void removeFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")                                             //Запрос на получение списка всех друзей
    public List<User> getFriendsList(@PathVariable("id") int userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")                           //запрос на получение списка общих друзей
    public List<User> getCommonFriendsList(@PathVariable("id") int userId, @PathVariable("otherId") int otherId) {
        return userService.getCommonFriendsList(userId, otherId);
    }
}