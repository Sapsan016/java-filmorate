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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserStorage userStorage;


    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {          //Передаем запрос на получение списка всех пользователей в хранилище
        return userStorage.getAllUsers();
    }

    public User getUserById(@PathVariable("id") int id) {          //Передаем запрос на полуение пользователя в хранилище
        return userStorage.getUserById(id);
    }

    public User createUser(@Valid @RequestBody User user) {       //Передаем запрос на создание пользователя в хранилище
        return userStorage.createUser(user);
    }

    public User updateUser(@Valid @RequestBody User user) throws ValidationException { //Передаем запрос на обновление
        return userStorage.updateUser(user);                                           //      пользователя в хранилище
    }

    public void deleteUserById(int id) {                          //Передаем запрос на удаление пользователя в хранилище
        userStorage.deleteUserById(id);
    }

    public void addFriend(int userId, int friendId) {                                              //добавление в друзья
        userStorage.addFriend(userId, friendId);
        log.info("Пользователь с Id = {} добавил пользователя с Id = {}  в друзья", userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {                                            //удаление из друзей
        userStorage.removeFriend(userId,friendId);
        log.info("Пользователь с Id = {} удалил пользователя с Id = {} из друзей", userId, friendId);
    }

    public List<User> getFriendsList(int userId) {                                    //вывод списка друзей пользователя
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<Integer> listOfIds = userStorage.getFriendIdsFromBD(userId);                           //Получаем список Id
        List<User> friendsList = new ArrayList<>();
        for(Integer id : listOfIds) {                                                    //Добавляем пользователей по Id
            friendsList.add(userStorage.getUserById(id));
        }
        log.info("У пользователя с Id= " + userId + " список друзей:" + friendsList);
        return friendsList;
    }

    public List<User> getCommonFriendsList(int userId, int anotherUserId) {                   //вывод списка общих друзей
        User user = userStorage.getUserById(userId);
        User anotherUser = userStorage.getUserById(anotherUserId);
        if (user == null || anotherUser == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<User> commonFriendsList = user.getFriendsIds()
                .stream()
                .filter(friendId -> anotherUser.getFriendsIds().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.info("У пользователей с Id= " + userId + " и Id= " + anotherUserId + "список общих друзей:" + commonFriendsList);
        return commonFriendsList;
    }
}