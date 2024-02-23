package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.film_like.InMemoryFilmLikeStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    FilmService filmService;
    Film film1;
    Film film2;
    Film film3;

    @BeforeEach
    void setUp() throws ValidationException {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(inMemoryFilmStorage, new InMemoryFilmLikeStorage(inMemoryFilmStorage));

        film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .build();
        film1.getLikes().addAll(List.of(1, 2, 3, 4, 5));

        Film.increaseIdCounter();

        film2 = Film.builder()
                .id(2)
                .name("film2")
                .description("film2 description")
                .duration(99)
                .releaseDate(LocalDate.now().minusYears(20))
                .build();
        film2.getLikes().addAll(List.of(1, 2, 3));

        Film.increaseIdCounter();

        film3 = Film.builder()
                .id(3)
                .name("film3")
                .description("film3 description")
                .duration(98)
                .releaseDate(LocalDate.now().minusYears(10))
                .build();
        film3.getLikes().addAll(List.of(1, 2));

        filmService.create(film1);
        filmService.create(film2);
    }

    @Test
    void shouldCreate() throws ValidationException, NotFoundException {
        filmService.create(film3);
        assertEquals(film3, filmService.findFilmById(film3.getId()));
        assertEquals(3, filmService.findAll().size());
    }

    @Test
    void shouldThrowExceptionWhileCreateNull() {
        assertThrows(ValidationException.class, () -> filmService.create(null));
    }

    @Test
    void shouldFindAll() {
        List<Film> films = filmService.findAll();
        assertEquals(2, films.size());
        assertTrue(films.containsAll(List.of(film1, film2)));
    }

    @Test
    void shouldUpdate() throws ValidationException, NotFoundException {
        film2.setName("film2 updated");
        filmService.update(film2);
        assertEquals(film2.getName(), filmService.findFilmById(film2.getId()).getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateIncorrectFilm() {
        assertThrows(NotFoundException.class, () -> filmService.update(film3));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> filmService.update(null));
    }

    @Test
    void shouldDeleteFilmById() throws ValidationException, NotFoundException {
        filmService.deleteFilmById(film2.getId());
        assertEquals(1, filmService.findAll().size());
        assertFalse(filmService.findAll().contains(film2));
    }

    @Test
    void shouldThrowExceptionWhenDeleteIncorrectFilm() {
        assertThrows(NotFoundException.class, () -> filmService.deleteFilmById(film3.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNull() {
        assertThrows(ValidationException.class, () -> filmService.deleteFilmById(null));
    }

    @Test
    void shouldLikeFilm() throws ValidationException, NotFoundException {
        filmService.likeFilm(film2.getId(), 4);
        assertTrue(filmService.findFilmById(film2.getId()).getLikes().contains(4));
    }

    @Test
    void shouldThrowExceptionWhenLikeIncorrectFilm() {
        assertThrows(NotFoundException.class, () -> filmService.likeFilm(film3.getId(), 3));
    }

    @Test
    void shouldThrowExceptionWhenLikeNull() {
        assertThrows(ValidationException.class, () -> filmService.likeFilm(null, 3));
    }

    @Test
    void shouldDeleteLike() throws ValidationException, NotFoundException {
        filmService.deleteLike(film2.getId(), 1);
        assertFalse(filmService.findFilmById(film2.getId()).getLikes().contains(1));
    }

    @Test
    void shouldThrowExceptionWhenDeleteLikeFromIncorrectFilm() {
        assertThrows(NotFoundException.class, () -> filmService.deleteLike(film3.getId(), 1));
    }

    @Test
    void shouldThrowExceptionWhenDeleteLikeFromNull() {
        assertThrows(ValidationException.class, () -> filmService.deleteLike(null, 1));
    }

    @Test
    void shouldFindPopularFilms() throws ValidationException {
        List<Film> popFilms = filmService.findPopularFilms(5);
        assertEquals(2, popFilms.size());
        assertEquals(film1, popFilms.get(0));
        assertEquals(film2, popFilms.get(1));
    }

    @Test
    void shouldFindFilmById() throws ValidationException, NotFoundException {
        Film foundFilm = filmService.findFilmById(film2.getId());
        assertEquals(film2, foundFilm);
    }

    @Test
    void shouldThrowExceptionWhenFindIncorrectFilm() {
        assertThrows(NotFoundException.class, () -> filmService.findFilmById(film3.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindNull() {
        assertThrows(ValidationException.class, () -> filmService.findFilmById(null));
    }
}