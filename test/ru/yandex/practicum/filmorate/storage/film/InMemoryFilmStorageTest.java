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
                .name("film2")
                .description("film2 description")
                .duration(99)
                .releaseDate(LocalDate.now().minusYears(20))
                .id(2)
                .build();
        film2.getLikes().addAll(List.of(1, 2, 3));

        Film.increaseIdCounter();

        film3 = Film.builder()
                .name("film3")
                .description("film3 description")
                .duration(98)
                .releaseDate(LocalDate.now().minusYears(10))
                .id(3)
                .build();
        film3.getLikes().addAll(List.of(1, 2));

        filmStorage.add(film1);
        filmStorage.add(film2);
    }
}