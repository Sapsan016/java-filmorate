package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Repository
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class DBUserStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User getUserById(int id) {                                                      //Получаем пользователя по id
        if (!users.containsKey(id)) {                                                     //проверяем наличие пользовтеля
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь с Id" + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public ArrayList<User> getAllUsers() {                                          //Получаем список всех пользователей
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(@Valid @RequestBody User user) {                              //Записываем пользователя в БД
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию добавляем
            String sqlQuery = "insert into USERS ( USER_NAME, EMAIL, LOGIN, BIRTHDAY) " +
                    "values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();                   // Переменная для получениЯ и хранения id
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getLogin());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            user.setId(keyHolder.getKey().intValue());                        //Задаем пользователю сгенерированый БД id
            log.info("Пользователь с Id = " + user.getId() + " создан");
        } else {
            log.error("Валидация не прошла");
            throw new ValidationException("Валидация не прошла");
        }
        return user;
    }

    @Override
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {          //Обновляем пользователя
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь с Id= " + user.getId() + " не найден");  //проверяем наличие пользователя
        }
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию обновляем
            Set<Integer> temp = users.get(user.getId()).getFriendsIds(); //Сохраняем список друзей перед обновлением
            user.setFriendsIds(temp);
            users.put(user.getId(), user);

            log.info("Пользователь с Id= " + user.getId() + " обновлен");
            return user;
        } else {
            log.error("Валидация не прошла");
            throw new ValidationException("Валидация не прошла");
        }
    }

    @Override
    public void deleteUserById(int id) {                                                         //Удаляем пользователя
        if (!users.containsKey(id)) {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь с Id=" + id + " не найден");  //проверяем наличие пользователя
        }
        users.remove(id);
        log.info("Пользователь с Id=" + id + " удален");
    }

    @Override
    public boolean validateUser(User user) {                                                   //Валидация пользователя
        if (user.getName() == null || user.getName().isBlank()) {  //Если имя не заполнено, то использем логин для имени
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {                                   //Валидация даты рождения
            log.error("Неверная дата рождения");
            throw new ValidationException("Неверная дата рождения");
        }
        return true;
    }
}
