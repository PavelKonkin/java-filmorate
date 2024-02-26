package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.List;

@Service
@Slf4j
public class MPAService {

    private final MPAStorage mpaStorage;

    @Autowired
    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MPA findMPAById(Integer id) throws ValidationException, NotFoundException {
        if (id == null) {
            log.debug("В запросе на получение рейтинга MPA пришел null id");
            throw new ValidationException();
        } else {
            return mpaStorage.find(id);
        }
    }

    public List<MPA> findAll() {
        return mpaStorage.findAll();
    }
}
