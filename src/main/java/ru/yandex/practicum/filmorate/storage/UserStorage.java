package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.List;
@Component
public interface UserStorage {
    User getUserById(int id);                                                                    //Получаем пользователя
    List<User> getAllUsers();                                                       //Получаем список всех пользователей
    User createUser(@Valid @RequestBody User user);                                               //Создаем пользователя
    User updateUser(@Valid @RequestBody User user) throws ValidationException;                  //Обновляем пользователя
    boolean validateUser(User user);                                                             //Проверка пользователя
    void deleteUserById(int id);                                                                  //Удаляем пользователя
    List<Integer> getFriendIdsFromBD(int id);                                       // Получаем список Id друзей из базы
    void addFriend(int userId, int friendId);                                                         //Добавляем друга
    void removeFriend(int userId, int friendId);                                                        //Удаляем друга
}
