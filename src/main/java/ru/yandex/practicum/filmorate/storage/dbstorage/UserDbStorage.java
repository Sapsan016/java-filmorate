package ru.yandex.practicum.filmorate.storage.dbstorage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;

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
