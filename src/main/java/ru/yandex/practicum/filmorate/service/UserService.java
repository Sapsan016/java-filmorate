package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ArrayList<User> getAllUsers() {          //Передаем запрос на получение списка всех пользователей в хранилище
        return userStorage.getAllUsers();
    }

    public User getUserById(@PathVariable("id") int id) {          //Передаем запрос на полуение пользователя в хранилище
        return userStorage.getUserById(id);
    }

    public User createUser(@Valid @RequestBody User user) {       //Передаем Запрос на создание пользователя в хранилище
        return userStorage.createUser(user);
    }

    public User updateUser(@Valid @RequestBody User user) throws ValidationException { //Передаем запрос на обновление
        return userStorage.updateUser(user);                                           //      пользователя в хранилище
    }

    public void deleteUserById(int id) {                          //Передаем запрос на удаление пользователя в хранилище
        userStorage.deleteUserById(id);
    }

    public void addFriend(int userId, int friendId) {                                              //добавление в друзья
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            log.error("The user was not found");
            throw new UserNotFoundException("The user was not found");
        }
        user.getFriendsIds().add(friendId);                                  //Добавляем Id пользователей в списки друзей
        friend.getFriendsIds().add(userId);
        log.info("The users with Id = " + userId + " and Id = " + friendId + " added as friends");
    }

    public void removeFriend(int userId, int friendId) {                                            //удаление из друзей
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            log.error("The user was not found");
            throw new UserNotFoundException("The user was not found");
        }
        user.getFriendsIds().remove(friendId);                         //Удаляем Id пользователей в обоих списках друзей
        friend.getFriendsIds().remove(userId);
        log.info("The users with Id = " + userId + " and Id = " + friendId + " are not friends anymore");
    }

    public List<User> getFriendsList(int userId) {                                     //вывод списка друзей пользователя
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.error("The user was not found");
            throw new UserNotFoundException("The user was not found");
        }
        List<User> friendsList = user.getFriendsIds()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.info("The user with Id= " + userId + "has the friends list:" + friendsList);
        return friendsList;
    }

    public List<User> getCommonFriendsList(int userId, int anotherUserId) {                   //вывод списка общих друзей
        User user = userStorage.getUserById(userId);
        User anotherUser = userStorage.getUserById(anotherUserId);
        if (user == null || anotherUser == null) {
            throw new UserNotFoundException("The user was not found");
        }
        List<User> commonFriendsList = user.getFriendsIds()
                .stream()
                .filter(friendId -> anotherUser.getFriendsIds().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.info("The user with Id= " + userId + " and the user with Id= " + anotherUserId +
                "has the common friends list:" + commonFriendsList);
        return commonFriendsList;
    }
}