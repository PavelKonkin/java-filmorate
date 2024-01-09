package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class FilmStorageTest<T extends FilmStorage> {
    T filmStorage;
    Film film1;
    Film film2;
    Film film3;



    @Test
    void shouldAdd() throws ValidationException {
        filmStorage.add(film3);
        assertEquals(3, filmStorage.findAll().size());
        assertTrue(filmStorage.findAll().contains(film3));
    }

    @Test
    void shouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> filmStorage.add(null));
    }

    @Test
    void shouldUpdate() throws NotFoundException, ValidationException {
        film1.setName("film1 updated");
        filmStorage.update(film1);
        assertEquals(film1.getName(), filmStorage.findAll().stream()
                .filter(f -> f.getId() == film1.getId())
                .findAny()
                .orElseThrow().getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidFilm() {
        film3.setName("film3 updated");
        assertThrows(NotFoundException.class, () -> filmStorage.update(film3));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        film3.setName("film3 updated");
        assertThrows(ValidationException.class, () -> filmStorage.update(null));
    }

    @Test
    void shouldDelete() throws NotFoundException, ValidationException {
        filmStorage.delete(film1.getId());
        assertEquals(1, filmStorage.findAll().size());
        assertFalse(filmStorage.findAll().contains(film1));
    }

    @Test
    void shouldThrowExceptionWhenDeleteInvalidFilm() {
        assertThrows(NotFoundException.class, () -> filmStorage.delete(film3.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNull() {
        assertThrows(ValidationException.class, () -> filmStorage.delete(null));
    }

    @Test
    void shouldFindAll() {
        List<Film> films = filmStorage.findAll();
        assertEquals(2, films.size());
        assertTrue(films.containsAll(List.of(film1, film2)));
    }
}