package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class MPA {

    int id;

    String name;

    @Override
    public String toString() {
        return "MPA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
