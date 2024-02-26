package ru.yandex.practicum.filmorate.integration.storage.film;

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

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testCreateFilm() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, new FilmGenreDbStorage(jdbcTemplate));
        // вызываем тестируемый метод
        int filmId = filmStorage.add(newFilm);

        Film savedFilm = filmStorage.get(filmId);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(newFilm);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateFilm() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, new FilmGenreDbStorage(jdbcTemplate));
        // вызываем тестируемый метод
        int filmId = filmStorage.add(newFilm);

        Film filmToUpdate = Film.builder()
                .name("film1_updated")
                .description("film1 updated description")
                .duration(119)
                .releaseDate(LocalDate.now().minusYears(20))
                .mpa(MPA.builder().id(2).name("PG").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(3).name("Мультфильм").build())))
                .id(filmId)
                .build();

        filmStorage.update(filmToUpdate);
        Film updatedFilm = filmStorage.get(filmId);

        // проверяем утверждения
        assertThat(updatedFilm)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(filmToUpdate);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testDeleteFilm() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, new FilmGenreDbStorage(jdbcTemplate));

        int filmId = filmStorage.add(newFilm);
        // вызываем тестируемый метод
        filmStorage.delete(filmId);

        // проверяем утверждения
        assertThat(newFilm)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isNotIn(filmStorage.findAll());        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindAll() throws ValidationException {
        // Подготавливаем данные для теста
        Film newFilm1 = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        Film newFilm2 = Film.builder()
                .name("film2")
                .description("film2 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, new FilmGenreDbStorage(jdbcTemplate));
        // вызываем тестируемый метод
        int filmId1 = filmStorage.add(newFilm1);
        int filmId2 = filmStorage.add(newFilm2);


        List<Film> films = filmStorage.findAll();
        // проверяем утверждения
        assertThat(films)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(List.of(newFilm1, newFilm2));        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGetFilm() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .genres(new TreeSet<>(List.of(Genre.builder().id(1).name("Комедия").build())))
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, new FilmGenreDbStorage(jdbcTemplate));
        // вызываем тестируемый метод
        int filmId = filmStorage.add(newFilm);

        Film savedFilm = filmStorage.get(filmId);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(newFilm);        // и сохраненного пользователя - совпадают
    }
}
