package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
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

    public void addFriend(int userId, int friendId) {                                              //добавление в друзья
       User user = userStorage.getUserById(userId);
       User friend = userStorage.getUserById(friendId);
       if(user == null || friend == null){
           log.error("User not found");
           throw new UserNotFoundException("User not found");
       }
       user.getFriendsIds().add(friendId);                                  //Добавляем Id пользователей в списки друзей
       friend.getFriendsIds().add(userId);
       log.info("Users with Id = " + userId + " and Id = " + friendId + " added as friends");

    }

    public void removeFriend(int userId, int friendId) {                                            //удаление из друзей
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if(user == null || friend == null){
            log.error("User not found");
            throw new UserNotFoundException("User not found");

        }
        user.getFriendsIds().remove(friendId);                         //Удаляем Id пользователей в обоих списках друзей
        friend.getFriendsIds().remove(userId);
        log.info("Users with Id = " + userId + " and Id = " + friendId + " not friends anymore");

    }
    public List<User> getFriendsList(int userId){                                     //вывод списка друзей пользователя
        User user = userStorage.getUserById(userId);
        if(user == null){
            throw new UserNotFoundException("User not found");
        }
        return user.getFriendsIds()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

    }

    public List<User> getCommonFriendsList(int userId, int anotherUserId){                   //вывод списка общих друзей
        User user = userStorage.getUserById(userId);
        User anotherUser = userStorage.getUserById(anotherUserId);
        if(user == null || anotherUser == null){
            throw new UserNotFoundException("User not found");
        }
        return user.getFriendsIds()
                .stream()
                .filter(friendId -> anotherUser.getFriendsIds().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
                }
}
