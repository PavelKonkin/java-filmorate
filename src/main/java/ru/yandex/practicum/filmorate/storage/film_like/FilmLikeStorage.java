package ru.yandex.practicum.filmorate.storage.film_like;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

public interface FilmLikeStorage {
    void add(Integer filmId, Integer userId) throws NotFoundException, ValidationException;

    void delete(Integer filmId, Integer userId) throws NotFoundException, ValidationException;
}
