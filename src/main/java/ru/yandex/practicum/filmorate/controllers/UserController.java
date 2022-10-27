package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    private int generatedId = 0;

    private int getGeneratedId() {
        return ++generatedId;
    }


    @GetMapping()
    public ArrayList<User> getAllUsers() {                                          //Получаем список всех пользователей
        return new ArrayList<>(users.values());
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {          //Обновляем пользователя
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("The user hasn't been created");            //Валидируем создание пользователя
        }
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию обновляем
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Validation failed");
        }
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {                                       //Создаем пользователя
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию добавляем
            user.setId(getGeneratedId());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Validation failed");
        }
    }

    private boolean validateUser(User user) {                                                   //Валидация пользователя
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //Форматтер

        LocalDate birthdayDate = LocalDate.parse(user.getBirthday(), formatter);
        LocalDateTime birthdayDateTime = LocalDateTime.of(birthdayDate, LocalDateTime.now().toLocalTime());

        if (user.getName() == null || user.getName().isBlank()) {  //Если имя не заполнено, то использем логин для имени
            user.setName(user.getLogin());
        }
        if (birthdayDateTime.isAfter(LocalDateTime.now())) {                                   //Валидация даты рождения
            throw new ValidationException("Invalid birthday");
        }
        return true;
    }
}