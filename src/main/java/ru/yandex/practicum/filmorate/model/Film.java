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
    Set<Integer> likes;               //Список id пользователей поставивших лайк фильму
    int rating;                       //Рейтинг фильма
    List<String> genres;              //Жанры фильма

    }