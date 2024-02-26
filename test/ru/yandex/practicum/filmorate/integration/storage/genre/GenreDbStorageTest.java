package ru.yandex.practicum.filmorate.integration.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAll() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        List<Genre> genres = genreDbStorage.findAll();

        assertThat(genres.size()).isEqualTo(6);
        assertThat(genres.get(0)).isEqualTo(Genre.builder().id(1).name("Комедия").build());
        assertThat(genres.get(1)).isEqualTo(Genre.builder().id(2).name("Драма").build());
        assertThat(genres.get(2)).isEqualTo(Genre.builder().id(3).name("Мультфильм").build());
        assertThat(genres.get(3)).isEqualTo(Genre.builder().id(4).name("Триллер").build());
        assertThat(genres.get(4)).isEqualTo(Genre.builder().id(5).name("Документальный").build());
        assertThat(genres.get(5)).isEqualTo(Genre.builder().id(6).name("Боевик").build());
    }

    @Test
    public void testFind() throws NotFoundException {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        Genre genre = genreDbStorage.find(3);

        assertThat(genre)
                .isNotNull()
                .isEqualTo(Genre.builder().id(3).name("Мультфильм").build());

    }
}
