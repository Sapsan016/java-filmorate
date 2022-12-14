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
import java.util.ArrayList;
import java.util.List;
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class DBUserStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public User getUserById(int id) {                                                      //Получаем пользователя по id
        if (isPresent(id)) {
            String sqlQuery = "select * from USERS where USER_ID = ?";
            User user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
            List<Integer> friendList = getFriendIdsFromBD(id).isEmpty()  //Если список id друзей не пустой добавляем пользователю
                    ? new ArrayList<>(): getFriendIdsFromBD(id);
            user.setFriendsIds(friendList);
            return user;
        }
        log.info("Пользователь с Id = {} не найден", id);
        throw new UserNotFoundException("Пользователь не найден");
    }
    @Override
    public List<User> getAllUsers() {                                          //Получаем список всех пользователей
        String sqlQuery = "select USER_ID, EMAIL, USER_NAME, LOGIN, BIRTHDAY from USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        for(User user : users) {
            user.setFriendsIds(getFriendIdsFromBD(user.getId()));
        }
        return users;
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
        user.setFriendsIds(new ArrayList<>());
        return user;
    }
    public User updateUser(@Valid @RequestBody User user) {                                    //Обновляем пользователя
        if (validateUser(user) && isPresent(user.getId())) {   //Если пользователь прошел валидацию и есть в базе обновляем
            List<Integer> temp = user.getFriendsIds();
            final String SQL = "update USERS set EMAIL = ?, USER_NAME = ?, LOGIN = ?, BIRTHDAY = ? " +
                    "where USER_ID = ?";
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
            final String sqlQuery = "delete from USERS where USER_ID = ?";
            jdbcTemplate.update(sqlQuery, id);
            log.info("Пользователь с Id= " + id + " удален");
        }
    }
    @Override
    public void addFriend(int userId, int friendId) {                                                 //Добавляем друга
        if(!isPresent(userId) || !isPresent(friendId)) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        final String SQL = "insert into FRIENDS (USER_ID, FRIEND_ID, STATUS) values (?, ?, ?)";
        jdbcTemplate.update(SQL, userId, friendId, false);
    }
    @Override
    public void removeFriend(int userId, int friendId) {                                                 //Удаляем друга
        if(!isPresent(userId) || !isPresent(friendId)) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        final String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
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
    @Override
    public List<Integer> getFriendIdsFromBD(int id) {                             // Получаем список Id друзей из базы
        final String friendSQL = "select FRIEND_ID from FRIENDS " +
                "where USER_ID = ?";
        return jdbcTemplate.query(friendSQL, this::mapRowToFriendsIds, id);
    }
    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException { // Метод для преобразования запроса
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .name(resultSet.getString("USER_NAME"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
    private boolean isPresent(int id) {                                           //Проверяем наличие пользователя в базе
        final String check = "select * from users where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(check, id);
        if (!userRows.next()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден");
        }
        return true;
    }
    private Integer mapRowToFriendsIds(ResultSet rs, int rowNum) throws SQLException {   //Преобразуем данные из БД в Id
        return rs.getInt("FRIEND_ID");
    }
}