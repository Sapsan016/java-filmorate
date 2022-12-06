package ru.yandex.practicum.filmorate.exception;

public class MotionCompanyNotFoundException extends RuntimeException {
    public MotionCompanyNotFoundException(String message) {
        super(message);
    }
}
