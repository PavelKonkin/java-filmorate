package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre findGenreById(Integer id) throws ValidationException, NotFoundException {
        if (id == null) {
            log.debug("В запросе на получение жанра пришел null id");
            throw new ValidationException();
        } else {
            return genreStorage.find(id);
        }
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }
}
