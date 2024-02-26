package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос на список всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    @Validated
    public Film findFilm(@PathVariable @NotNull Integer id) throws NotFoundException, ValidationException {
        return filmService.findFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film != null) {
            return filmService.create(film);
        } else {
            log.debug("Ошибка валидации фильма: пришел null");
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        if (film != null) {
            filmService.update(film);
            return film;
        } else {
            log.debug("Ошибка валидации фильма: пришел null");
            throw new ValidationException();
        }
    }

    @DeleteMapping("/{id}")
    @Validated
    public void delete(@PathVariable @NotNull Integer id) throws NotFoundException, ValidationException {
        filmService.deleteFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @Validated
    public void likeFilm(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer userId)
            throws NotFoundException, ValidationException {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Validated
    public void deleteLike(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer userId)
            throws NotFoundException, ValidationException {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count)
            throws ValidationException {
        if (count == null || count < 0) {
            throw new ValidationException();
        }
        return filmService.findPopularFilms(count);
    }

}
