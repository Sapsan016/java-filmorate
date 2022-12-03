package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
        if (isPresent(id)) {
            String sqlQuery = "select USER_ID, EMAIL, USER_NAME, LOGIN, BIRTHDAY " +
                    "from USERS where USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        }
        throw new UserNotFoundException("Пользователь не найден");
    }


    @Override
    public List<User> getAllUsers() {                                          //Получаем список всех пользователей
        String sqlQuery = "select USER_ID, EMAIL, USER_NAME, LOGIN, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User createUser(@Valid @RequestBody User user) {                              //Записываем пользователя в БД
        if (validateUser(user)) {                                         //Если пользователь прошел валидацию добавляем
            String sqlQuery = "insert into USERS (EMAIL, USER_NAME, LOGIN, BIRTHDAY) " +
                    "values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();                   // Переменная для получениЯ и хранения id
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getLogin());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            user.setId(keyHolder.getKey().intValue());                        //Задаем пользователю сгенерированый БД id
            log.info("Пользователь с Id = {} создан", user.getId());
        } else {
            log.error("Валидация не прошла");
            throw new ValidationException("Валидация не прошла");
        }
        user.setFriendsIds(new HashSet<>());
        return user;
    }

    public User updateUser(@Valid @RequestBody User user) {                                    //Обновляем пользователя
        if (validateUser(user) && isPresent(user.getId())) {   //Если пользователь прошел валидацию и есть в базе обновляем
            Set<Integer> temp = new HashSet<>();
            final String SQL = "UPDATE users SET EMAIL = ?, USER_NAME = ?, LOGIN = ?, BIRTHDAY = ? " +
                    "WHERE USER_ID = ?";
            jdbcTemplate.update(SQL,
                    user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
            log.info("Пользователь с Id = {} обновлен", user.getId());
            user.setFriendsIds(temp);
            return user;
        } else {
            log.error("Валидация не прошла");
            throw new ValidationException("Валидация не прошла");
        }
    }

    @Override
    public void deleteUserById(int id) {                                                         //Удаляем пользователя
        if (isPresent(id)) {
            String sqlQuery = "delete from USERS where USER_ID = ?";
            jdbcTemplate.update(sqlQuery, id);
            log.info("Пользователь с Id= " + id + " удален");
        }
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

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException { // Метод для преобразования запроса
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .name(resultSet.getString("USER_NAME"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .friendsIds(new HashSet<>())
                .build();
    }

    private boolean isPresent(int id) {                                           //Проверяем наличие пользователя в базе
        final String CHECK = "SELECT * FROM users WHERE USER_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(CHECK, id);
        if (!filmRows.next()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден");
        }
        return true;
    }
}
