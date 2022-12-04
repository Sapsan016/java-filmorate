package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                     //Обработка ошибки пользователь не найден
    public ErrorResponse userNotFoundExceptionResponse (final UserNotFoundException e){
        return new ErrorResponse(e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                            //Обработка ошибки фильм не найден
    public ErrorResponse filmNotFoundExceptionResponse (final FilmNotFoundException e){
        return new ErrorResponse(e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                            //Обработка ошибки фильм не найден
    public ErrorResponse mpaNotFoundException (final MpaNotFoundException e){
        return new ErrorResponse(e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                                //Обработка ошибки валидации
    public ErrorResponse invalidValidationExceptionResponse (final ValidationException e){
        return new ErrorResponse(e.getMessage()
        );
    }
    @ExceptionHandler                                                                         //Обработка прочих ошибок
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableExceptionResponse (final Throwable e){
        return new ErrorResponse("Возникла непредвиденная ошибка!!!"
        );
    }
}
