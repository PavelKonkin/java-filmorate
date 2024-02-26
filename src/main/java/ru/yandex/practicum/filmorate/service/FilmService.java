package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_like.FilmLikeStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmLikeStorage filmLikeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("filmLikeDbStorage") FilmLikeStorage filmLikeStorage) {
        this.filmStorage = filmStorage;
        this.filmLikeStorage = filmLikeStorage;
    }

    public Film create(Film film) throws ValidationException {
            Film.increaseIdCounter();
            int id = filmStorage.add(film);
            Film createdFilm = film.toBuilder().id(id).build();
            log.debug("Создан фильм {}", createdFilm);
            return createdFilm;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void update(Film film) throws NotFoundException, ValidationException {
        filmStorage.update(film);
        log.debug("Обновлен фильм {}", film);
    }

    public void deleteFilmById(Integer id) throws NotFoundException, ValidationException {
        filmStorage.delete(id);
        log.debug("Удален фильм с id {}", id);
    }

    public void likeFilm(Integer id, Integer userId) throws NotFoundException, ValidationException {
        filmLikeStorage.add(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundException, ValidationException {
        filmLikeStorage.delete(id, userId);
    }

    public List<Film> findPopularFilms(Integer count) throws ValidationException {
        return filmStorage.getPopular(count);
    }

    public Film findFilmById(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        return filmStorage.get(id);
    }

}
