package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final static LocalDate MIN_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);
    private final static int MIN_DURATION = 1;
    private final static int MAX_DESCRIPTION_LENGTH = 200;


    @GetMapping
    public List<Film> findAll() {
        log.info("Получен запрос на список всех фильмов");
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (validate(film)) {
            log.info("Создан фильм {}", film);
            films.put(film.getId(), film);
            return film;
        } else {
            Film.setBackIdCounter();
            log.info("Ошибка валидации фильма {}", film);
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        Film.setBackIdCounter();
        if (validate(film) && films.containsKey(film.getId())) {
            log.info("Обновлен фильм {}", film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.info("Ошибка валидации фильма {}", film);
            throw new ValidationException();
        }
    }

    private boolean validate(Film film) {
        if (film == null) {
            return false;
        }
        boolean isNameValid = film.getName() != null && !film.getName().isBlank();
        boolean isDescriptionValid = film.getDescription() != null
                && film.getDescription().length() <= MAX_DESCRIPTION_LENGTH;
        boolean isReleaseDateValid = film.getReleaseDate() != null
                && film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean isDurationValid = film.getDuration() >= MIN_DURATION;
        return isNameValid && isDescriptionValid && isReleaseDateValid && isDurationValid;
    }

}
