package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MpaController {
    final MpaService mpaService;
    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }
    @GetMapping()
    public List<MPA> getAllMpa() {                                       //Запрос на получение списка всех рейтингов МРА
        return mpaService.getAll();
    }               //Эндпойнт для получения списка всех МРА
    @GetMapping("/{id}")                                                     //Эндпойнт на получение рейтинга MPA по Id
    public MPA getMpaById(@PathVariable("id") int id){
        return mpaService.getMpaById(id);
    }
}
