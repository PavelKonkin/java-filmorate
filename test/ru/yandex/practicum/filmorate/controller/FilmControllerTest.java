package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.film_like.InMemoryFilmLikeStorage;

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
        InMemoryFilmStorage inMemoryFilmStorage1 = new InMemoryFilmStorage();
        InMemoryFilmStorage inMemoryFilmStorage2 = new InMemoryFilmStorage();
        filmControllerWithFilms = new FilmController(new FilmService(inMemoryFilmStorage1,
                new InMemoryFilmLikeStorage(inMemoryFilmStorage1)));
        filmControllerWithoutFilms = new FilmController(new FilmService(inMemoryFilmStorage2,
                new InMemoryFilmLikeStorage(inMemoryFilmStorage2)));

        film1 = Film.builder()
                .description("film1")
                .name("film1")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(1)
                .build();
        filmControllerWithFilms.create(film1);

        film2 = Film.builder()
                .description("film2")
                .name("film2")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(2)
                .build();
        filmControllerWithFilms.create(film2);

        filmControllerWithFilms.likeFilm(film2.getId(), 1);

        film3 = Film.builder()
                .description("film3")
                .name("film3")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(3)
                .build();


        invalidDurationFilm = Film.builder()
                .description("invalidDurationFilm")
                .name("invalidDurationFilm")
                .duration(-100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(4)
                .build();


        invalidReleaseDateFilm = Film.builder()
                .description("invalidReleaseDateFilm")
                .name("invalidReleaseDateFilm")
                .duration(100)
                .releaseDate(LocalDate.of(1850, 1,1))
                .id(5)
                .build();


        invalidDescriptionLengthFilm = Film.builder()
                .description("invalidDescriptionLengthFilm".repeat(10))
                .name("invalidDescriptionLengthFilm")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(6)
                .build();


        invalidNameFilm = Film.builder()
                .description("invalidNameFilm")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(7)
                .build();
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
        assertDoesNotThrow(() -> filmControllerWithFilms.delete(film1.getId()));
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