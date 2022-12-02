package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    int id;
    @NotBlank                                             //Проверка email на пустую строку, null и соответствие формату
    @NotNull
    @Email
    String email;
    String name;
    @NotBlank                                                                            //Проверка на заполнение логина
    @NotNull
    String login;
    @NotNull                                                                      //Проверка на заполнение даты рождения
    LocalDate birthday;

    Set<Integer> friendsIds;                                                               //Поле для хранения Id друзей

}