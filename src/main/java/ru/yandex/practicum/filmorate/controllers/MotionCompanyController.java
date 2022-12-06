package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MotionCompany;
import ru.yandex.practicum.filmorate.service.MotionCompanyService;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotionCompanyController {
    final MotionCompanyService motionCompanyService;
    @Autowired
    public MotionCompanyController(MotionCompanyService motionCompanyService) {
        this.motionCompanyService = motionCompanyService;
    }
    @GetMapping()
    public List<MotionCompany> getAllMpa() {                                       //Запрос на получение списка всех рейтингов МРА
        return motionCompanyService.getAll();
    }               //Эндпойнт для получения списка всех МРА
    @GetMapping("/{id}")                                                     //Эндпойнт на получение рейтинга MotionCompany по Id
    public MotionCompany getMpaById(@PathVariable("id") int id){
        return motionCompanyService.getMpaById(id);
    }
}
