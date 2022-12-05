package ru.yandex.practicum.filmorate.model;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    List<Integer> likes;               //Список id пользователей поставивших лайк фильму
    MPA mpa;                         //Рейтинг фильма
    List<Genre> genres;              //Жанры фильма


    }
