package ru.yandex.practicum.filmorate.controllers;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)
public class GenreController {
    GenreService genreService;
    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
    @GetMapping()
    public List<Genre> getAllGenres() {                                         //Эндпойнт для получение списка всех жанров
        return genreService.getAll();
    }
    @GetMapping("/{id}")                                                          //Эндпойнт для получение жанра по Id
    public Genre getGenreById(@PathVariable("id") int id){
        return genreService.getGenreById(id);
    }
}
