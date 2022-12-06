package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.MotionCompany;
import ru.yandex.practicum.filmorate.storage.MotionCompanyStorage;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)

public class MotionCompanyService {

    MotionCompanyStorage motionCompanyStorage;

    public MotionCompanyService(MotionCompanyStorage motionCompanyStorage) {
        this.motionCompanyStorage = motionCompanyStorage;
    }

    public List<MotionCompany> getAll() {
        return motionCompanyStorage.getAllMpa();
    }

    public MotionCompany getMpaById(@PathVariable("id")int id) {
        return motionCompanyStorage.getMpaById(id);
    }
}
