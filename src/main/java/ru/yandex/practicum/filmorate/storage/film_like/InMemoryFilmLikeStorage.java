package ru.yandex.practicum.filmorate.storage.film_like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Set;

@Component
@Slf4j
@Qualifier("inMemoryFilmLikeStorage")
public class InMemoryFilmLikeStorage implements FilmLikeStorage {
    private final FilmStorage filmStorage;

    public InMemoryFilmLikeStorage(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    @Override
    public void add(Integer filmId, Integer userId) throws NotFoundException, ValidationException {
        if (filmId == null) {
            throw new ValidationException();
        }
        Film film = filmStorage.get(filmId);
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
        filmStorage.update(film);
    }

    @Override
    public void delete(Integer filmId, Integer userId) throws NotFoundException, ValidationException {
        if (filmId == null) {
            throw new ValidationException();
        }
        Film film = filmStorage.get(filmId);
        Set<Integer> likes = film.getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            throw new NotFoundException();
        }
        filmStorage.update(film);
    }
}
