package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

public class InMemoryUserStorage implements UserStorage {
    @Autowired
    public InMemoryUserStorage() {

    }

    int generatedId = 0;
    final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public int getGeneratedId() {
        return ++generatedId;
    }

    @Override
    public User getUserById(int id) {                                                      //Получаем пользователя по id
        if(!users.containsKey(id)){                                                     //проверяем наличие пользовтеля
            log.error("User not found");
            throw new UserNotFoundException("User with Id" + id + "not found");
        }
        return users.get(id);
    }

    @Override
    public ArrayList<User> getAllUsers() {                                          //Получаем список всех пользователей
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(@Valid @RequestBody User user) {                                       //Создаем пользователя
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию добавляем
            user.setId(getGeneratedId());                                 //Создаем Id и пустой список друзей
            user.setFriendsIds(new HashSet<>());
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Validation failed");
            throw new ValidationException("Validation failed");
        }
    }

    @Override
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {          //Обновляем пользователя
        if (!users.containsKey(user.getId())) {
            log.error("User not found");
            throw new UserNotFoundException("User with Id" + user.getId() + "not found");  //проверяем наличие пользователя
        }
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию обновляем
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Validation failed");
            throw new ValidationException("Validation failed");
        }
    }

    @Override
    public boolean validateUser(User user) {                                                   //Валидация пользователя
        if (user.getName() == null || user.getName().isBlank()) {  //Если имя не заполнено, то использем логин для имени
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {                                   //Валидация даты рождения
            log.error("Invalid birthday");
            throw new ValidationException("Invalid birthday");
        }
        return true;
    }
}
