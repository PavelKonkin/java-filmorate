package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void create(Film film) throws ValidationException {
            Film.increaseIdCounter();
            log.debug("Создан фильм {}", film);
            filmStorage.add(film);
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
    }

    public void likeFilm(Integer id, Integer userId) throws NotFoundException, ValidationException {
        Film film = findFilmById(id);
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundException, ValidationException {
        Film film = findFilmById(id);
        Set<Integer> likes = film.getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            throw new NotFoundException();
        }
        filmStorage.update(film);
    }

    public List<Film> findPopularFilms(Integer count) throws ValidationException {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        return filmStorage.findAll().stream()
                .filter(f -> f.getId() == id)
                .findAny().orElseThrow(NotFoundException::new);
    }

}
