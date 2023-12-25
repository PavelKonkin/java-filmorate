package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос на список всех фильмов");
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film != null) {
            Film.increaseIdCounter();
            log.debug("Создан фильм {}", film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.debug("Ошибка валидации фильма {}", film);
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film != null && films.containsKey(film.getId())) {
            log.debug("Обновлен фильм {}", film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.debug("Ошибка валидации фильма {}", film);
            throw new ValidationException();
        }
    }
}
