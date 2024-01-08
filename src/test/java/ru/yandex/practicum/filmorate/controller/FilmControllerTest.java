package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void initializeTestData() throws ValidationException, NotFoundException {
        filmControllerWithFilms = new FilmController(new FilmService(new InMemoryFilmStorage()));
        filmControllerWithoutFilms = new FilmController(new FilmService(new InMemoryFilmStorage()));

        film1 = new Film();
        film1.setDescription("film1");
        film1.setName("film1");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.now().minusYears(10));
        filmControllerWithFilms.create(film1);

        film2 = new Film();
        film2.setDescription("film2");
        film2.setName("film2");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.now().minusYears(10));
        filmControllerWithFilms.create(film2);

        filmControllerWithFilms.likeFilm(film2.getId(), 1);

        film3 = new Film();
        film3.setDescription("film3");
        film3.setName("film3");
        film3.setDuration(100);
        film3.setReleaseDate(LocalDate.now().minusYears(10));

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
    void shouldReturnFilmsList() {
        assertTrue(filmControllerWithFilms.findAll().containsAll(List.of(film1, film2)));
    }

    @Test
    void shouldFindFilmById() throws NotFoundException, ValidationException {
        assertEquals(film1, filmControllerWithFilms.findFilm(film1.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindByIncorrectId() {
        assertThrows(NotFoundException.class, () -> filmControllerWithFilms.findFilm(-2));
    }

    @Test
    void shouldThrowExceptionWhenFindByNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.findFilm(null));
    }

    @Test
    void shouldThrowExceptionWhenCreateNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(null));
    }

    @Test
    void shouldFailValidationWhenCreateInvalidDurationFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(invalidDurationFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenCreateInvalidReleaseDateFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(invalidReleaseDateFilm);
        assertEquals(1, violations.size());
        //assertThrows(ValidationException.class, () -> filmControllerWithFilms.create(invalidReleaseDateFilm));
    }

    @Test
    void shouldFailValidationWhenCreateInvalidDescriptionLengthFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(invalidDescriptionLengthFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenCreateInvalidNameFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(invalidNameFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.update(null));
    }

    @Test
    void shouldFailValidationWhenUpdateInvalidDurationFilm() {
        film1.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenUpdateInvalidReleaseDateFilm() {
        film1.setReleaseDate(LocalDate.of(1850, 1,1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenUpdateInvalidDescriptionLengthFilm() {
        film1.setDescription("invalidDescriptionLengthFilm".repeat(10));
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenUpdateInvalidNameFilm() {
        film1.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldCreateFilm() throws ValidationException {
        Film filmCreated = filmControllerWithFilms.create(film3);
        assertEquals(film3, filmCreated);
    }

    @Test
    void shouldUpdateFilm() throws ValidationException, NotFoundException {
        film2.setDescription("New description");
        filmControllerWithFilms.update(film2);
        assertEquals(Objects.requireNonNull(filmControllerWithFilms.findAll().stream()
                .filter(film -> film.getId() == film2.getId())
                .findFirst()
                .orElse(null)).getDescription(), film2.getDescription());
    }

    @Test
    void shouldDelete() {
        assertDoesNotThrow(() ->filmControllerWithFilms.delete(film1.getId()));
        assertEquals(1, filmControllerWithFilms.findAll().size());
        assertThrows(NotFoundException.class, () -> filmControllerWithFilms.findFilm(film1.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteIncorrectId() {
        assertThrows(NotFoundException.class, () -> filmControllerWithFilms.delete(-5));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.delete(null));
    }

    @Test
    void shouldAddLike() throws NotFoundException, ValidationException {
        Integer userId = 1;
        filmControllerWithFilms.likeFilm(film1.getId(), userId);
        Film likedFilm = filmControllerWithFilms.findFilm(film1.getId());
        assertEquals(1, likedFilm.getLikes().size());
        assertTrue(likedFilm.getLikes().contains(userId));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectFilmId() {
        assertThrows(NotFoundException.class, () -> filmControllerWithFilms.likeFilm(9999, 1));
    }

    @Test
    void shouldThrowExceptionWhenLikeNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.likeFilm(null, null));
    }

    @Test
    void shouldDeleteLike() throws NotFoundException, ValidationException {
        filmControllerWithFilms.deleteLike(film2.getId(), 1);
        assertEquals(0, filmControllerWithFilms.findFilm(film2.getId()).getLikes().size());
    }

    @Test
    void shouldThrowExceptionWhenDeleteLikeWithIncorrectId() {
        assertThrows(NotFoundException.class, () -> filmControllerWithFilms.deleteLike(9999, 2));
    }

    @Test
    void shouldThrowExceptionWhenDeleteLikeNull() {
        assertThrows(ValidationException.class, () -> filmControllerWithFilms.deleteLike(null, null));
    }

    @Test
    void shouldReturnPopularFilms() throws ValidationException {
        List<Film> films = filmControllerWithFilms.findPopularFilms(11);
        assertEquals(2, films.size());
        assertEquals(film2, films.get(0));
    }
}