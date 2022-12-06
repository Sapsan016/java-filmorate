package ru.yandex.practicum.filmorate.model;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

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
    List<Integer> likes;              //Список id пользователей поставивших лайк фильму
    MotionCompany mpa;                         //Рейтинг фильма
    List<Genre> genres;              //Жанры фильма


    }
