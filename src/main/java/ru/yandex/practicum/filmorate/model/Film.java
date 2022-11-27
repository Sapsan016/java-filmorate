package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes;               //Список id пользователей поставивших лайк фильму
    String rating;                    //Рейтинг фильма
    List<String> genres;              //Жанры фильма

}
