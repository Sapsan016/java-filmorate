package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank                          //Проверка email на пустую строку, null и соответствие формату
    @NotNull
    @Email
    private String email;
    private String name;
    @NotBlank                         //Проверка на заполнение логина
    @NotNull
    private String login;
    @NotBlank                         //Проверка на заполнение даты рождения
    @NotNull
    private String birthday;
}