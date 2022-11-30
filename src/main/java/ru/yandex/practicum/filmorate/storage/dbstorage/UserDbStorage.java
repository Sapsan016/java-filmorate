package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
@Repository
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)
//@Qualifier("userStorage")
@Primary

public class UserDbStorage implements UserStorage {
    @Override
    public int getGeneratedId() {
        return 0;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        return null;
    }

    @Override
    public boolean validateUser(User user) {
        return false;
    }

    @Override
    public void deleteUserById(int id) {

    }
}
