package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос на список всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Integer id) throws NotFoundException, ValidationException {
        if (id != null) {
            return filmService.findFilmById(id);
        } else {
            throw new ValidationException();
        }
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film != null) {
            filmService.create(film);
            return film;
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
    public void delete(@PathVariable Integer id) throws NotFoundException, ValidationException {
        if (id != null) {
            filmService.deleteFilmById(id);
        } else {
            throw new ValidationException();
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId)
            throws NotFoundException, ValidationException {
        if (id != null && userId != null) {
            filmService.likeFilm(id, userId);
        } else {
            throw new ValidationException();
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundException, ValidationException {
        if (id != null && userId != null) {
            filmService.deleteLike(id, userId);
        } else {
            throw new ValidationException();
        }
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count)
            throws ValidationException {
        return filmService.findPopularFilms(count);
    }

}
