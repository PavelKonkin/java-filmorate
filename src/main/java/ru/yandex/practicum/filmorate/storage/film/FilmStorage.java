package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int add(Film film) throws ValidationException;

    void update(Film film) throws NotFoundException, ValidationException;

    void delete(Integer id) throws NotFoundException, ValidationException;

    List<Film> findAll();

    Film get(int id) throws NotFoundException;

    List<Film> getPopular(Integer count);
}
