package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    int id;
    @NotBlank                          //Проверка email на пустую строку, null и соответствие формату
    @NotNull
    @Email
    String email;
    String name;
    @NotBlank                         //Проверка на заполнение логина
    @NotNull
    String login;
    @NotBlank                         //Проверка на заполнение даты рождения
    @NotNull
    String birthday;
}