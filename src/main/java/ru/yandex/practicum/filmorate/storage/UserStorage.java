package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
@Component
public interface UserStorage {

    int getGeneratedId();

    User getUserById(int id);

    ArrayList<User> getAllUsers();                                                  //Получаем список всех пользователей

    User createUser(@Valid @RequestBody User user);                                               //Создаем пользователя

    User updateUser(@Valid @RequestBody User user) throws ValidationException;                  //Обновляем пользователя

    boolean validateUser(User user);

    void deleteUserById(int id);
}
