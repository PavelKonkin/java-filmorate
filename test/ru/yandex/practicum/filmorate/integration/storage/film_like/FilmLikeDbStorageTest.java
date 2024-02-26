package ru.yandex.practicum.filmorate.integration.storage.film_like;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film_like.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.film_like.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikeDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAdd() throws ValidationException, NotFoundException {
        FilmLikeStorage filmLikeStorage = new FilmLikeDbStorage(jdbcTemplate);
        FilmGenreDbStorage filmGenreDbStorage = new FilmGenreDbStorage(jdbcTemplate);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, filmGenreDbStorage);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .build();

        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        int filmId = filmStorage.add(newFilm);
        int userId = userStorage.add(newUser);

        // вызываем тестируемый метод
        filmLikeStorage.add(filmId, userId);
        String sql = "select * from film_like where film_id = ? and user_id = ?";
        int count = jdbcTemplate.queryForList(sql, filmId, userId).size();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void testDelete() throws ValidationException, NotFoundException {
        FilmLikeStorage filmLikeStorage = new FilmLikeDbStorage(jdbcTemplate);
        FilmGenreDbStorage filmGenreDbStorage = new FilmGenreDbStorage(jdbcTemplate);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, filmGenreDbStorage);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        Film newFilm = Film.builder()
                .name("film1")
                .description("film1 description")
                .duration(100)
                .releaseDate(LocalDate.now().minusYears(30))
                .mpa(MPA.builder().id(1).name("G").build())
                .build();

        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        int filmId = filmStorage.add(newFilm);
        int userId = userStorage.add(newUser);

        String sql = "select * from film_like where film_id = ? and user_id = ?";

        int countBefore = jdbcTemplate.queryForList(sql, filmId, userId).size();
        assertThat(countBefore).isEqualTo(0);

        filmLikeStorage.add(filmId, userId);

        int count = jdbcTemplate.queryForList(sql, filmId, userId).size();
        assertThat(count).isEqualTo(1);

        // вызываем тестируемый метод
        filmLikeStorage.delete(filmId, userId);
        int countAfter = jdbcTemplate.queryForList(sql, filmId, userId).size();
        assertThat(countAfter).isEqualTo(0);
    }


}
