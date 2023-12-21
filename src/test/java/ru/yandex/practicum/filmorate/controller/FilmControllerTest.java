package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmControllerWithFilms;
    FilmController filmControllerWithoutFilms;
    Film film1;
    Film film2;
    Film film3;
    Film invalidDurationFilm;
    Film invalidReleaseDateFilm;
    Film invalidDescriptionLengthFilm;
    Film invalidNameFilm;

    @BeforeEach
    void initializeTestData() throws ValidationException {
        filmControllerWithFilms = new FilmController();
        filmControllerWithoutFilms = new FilmController();

        film1 = new Film();
        film1.setDescription("film1");
        film1.setName("film1");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.now().minusYears(10));


        film2 = new Film();
        film2.setDescription("film2");
        film2.setName("film2");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.now().minusYears(10));


        film3 = new Film();
        film3.setDescription("film3");
        film3.setName("film3");
        film3.setDuration(100);
        film3.setReleaseDate(LocalDate.now().minusYears(10));


        filmControllerWithFilms.create(film1);
        filmControllerWithFilms.create(film2);

        invalidDurationFilm = new Film();
        invalidDurationFilm.setDescription("invalidDurationFilm");
        invalidDurationFilm.setName("invalidDurationFilm");
        invalidDurationFilm.setDuration(-100);
        invalidDurationFilm.setReleaseDate(LocalDate.now().minusYears(10));


        invalidReleaseDateFilm = new Film();
        invalidReleaseDateFilm.setDescription("invalidReleaseDateFilm");
        invalidReleaseDateFilm.setName("invalidReleaseDateFilm");
        invalidReleaseDateFilm.setDuration(100);
        invalidReleaseDateFilm.setReleaseDate(LocalDate.of(1850, 1,1));


        invalidDescriptionLengthFilm = new Film();
        invalidDescriptionLengthFilm.setDescription("invalidDescriptionLengthFilm".repeat(10));
        invalidDescriptionLengthFilm.setName("invalidDescriptionLengthFilm");
        invalidDescriptionLengthFilm.setDuration(100);
        invalidDescriptionLengthFilm.setReleaseDate(LocalDate.now().minusYears(10));


        invalidNameFilm = new Film();
        invalidNameFilm.setDescription("invalidNameFilm");
        invalidNameFilm.setDuration(100);
        invalidNameFilm.setReleaseDate(LocalDate.now().minusYears(10));

    }

    @Test
    void shouldReturnEmptyList() {
        assertEquals(List.of(), filmControllerWithoutFilms.findAll());
    }

    @Test
    void shouldReturnUsersList() {
        assertEquals(List.of(film1, film2), filmControllerWithFilms.findAll());
    }

    @Test
    void shouldThrowExceptionWhenCreateNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(null));
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidDurationFilm() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(invalidDurationFilm));
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidReleaseDateFilm() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(invalidReleaseDateFilm));
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidDescriptionLengthFilm() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(invalidDescriptionLengthFilm));
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidNameFilm() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(invalidNameFilm));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidDurationFilm() {
        film1.setDuration(-100);
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(film1));
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidReleaseDateFilm() {
        film1.setReleaseDate(LocalDate.of(1850, 1,1));
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(film1));
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidDescriptionLengthFilm() {
        film1.setDescription("invalidDescriptionLengthFilm".repeat(10));
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(film1));
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidNameFilm() {
        film1.setName("");
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(film1));
    }

    @Test
    void shouldCreateFilm() throws ValidationException {
        Film filmCreated = filmControllerWithFilms.create(film3);
        assertEquals(film3, filmCreated);
    }

    @Test
    void shouldUpdateFilm() throws ValidationException {
        film2.setDescription("New description");
        filmControllerWithFilms.update(film2);
        assertEquals(Objects.requireNonNull(filmControllerWithFilms.findAll().stream()
                .filter(film -> film.getId() == film2.getId())
                .findFirst()
                .orElse(null)).getDescription(), film2.getDescription());
    }
}