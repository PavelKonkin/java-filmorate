package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class InMemoryFilmStorageTest extends FilmStorageTest<InMemoryFilmStorage> {

    @BeforeEach
    void setUp() throws ValidationException {
        filmStorage = new InMemoryFilmStorage();

        film1 = new Film();
        film1.setName("film1");
        film1.setDescription("film1 description");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.now().minusYears(30));
        film1.getLikes().addAll(List.of(1, 2, 3, 4, 5));

        Film.increaseIdCounter();

        film2 = new Film();
        film2.setName("film2");
        film2.setDescription("film2 description");
        film2.setDuration(99);
        film2.setReleaseDate(LocalDate.now().minusYears(20));
        film2.getLikes().addAll(List.of(1, 2, 3));

        Film.increaseIdCounter();

        film3 = new Film();
        film3.setName("film3");
        film3.setDescription("film3 description");
        film3.setDuration(98);
        film3.setReleaseDate(LocalDate.now().minusYears(10));
        film3.getLikes().addAll(List.of(1, 2));

        filmStorage.add(film1);
        filmStorage.add(film2);
    }
}