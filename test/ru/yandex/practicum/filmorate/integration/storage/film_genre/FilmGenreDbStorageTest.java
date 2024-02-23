package ru.yandex.practicum.filmorate.integration.storage.film_genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDbStorage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testCreate() throws ValidationException, NotFoundException {
        FilmGenreDbStorage filmGenreDbStorage = new FilmGenreDbStorage(jdbcTemplate);
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, filmGenreDbStorage);

        int filmId = filmStorage.add(newFilm);

        // вызываем тестируемый метод
        filmGenreDbStorage.create(filmId, List.of(1,2));

        Film filmWithGenres = filmStorage.get(filmId);
        assertThat(filmWithGenres.getGenres())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build(),
                        Genre.builder().id(2).name("Драма").build())));
    }

    @Test
    public void testUpdate() throws ValidationException, NotFoundException {
        FilmGenreDbStorage filmGenreDbStorage = new FilmGenreDbStorage(jdbcTemplate);
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build(),
                        Genre.builder().id(2).name("Драма").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, filmGenreDbStorage);

        int filmId = filmStorage.add(newFilm);

        // вызываем тестируемый метод
        filmGenreDbStorage.update(filmId, List.of(3,4));

        Film filmWithGenres = filmStorage.get(filmId);
        assertThat(filmWithGenres.getGenres())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new TreeSet<>(List.of(Genre.builder().id(3).name("Мультфильм").build(),
                        Genre.builder().id(4).name("Триллер").build())));
    }
}
