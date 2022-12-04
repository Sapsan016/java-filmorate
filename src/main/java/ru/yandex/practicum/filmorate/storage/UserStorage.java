package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
@Component
public interface UserStorage {

    User getUserById(int id);
    List<User> getAllUsers();                                                  //Получаем список всех пользователей
    User createUser(@Valid @RequestBody User user);                                               //Создаем пользователя
    User updateUser(@Valid @RequestBody User user) throws ValidationException;                  //Обновляем пользователя
    boolean validateUser(User user);
    void deleteUserById(int id);
    public List<Integer> getFriendIdsFromBD(int id);                            // Получаем список Id друзей из базы
    void addFriend(int userId, int friendId);                                                    //Добавляем лайк
    void removeFriend(int userId, int friendId);                                                 //Удаляем лайк

}
