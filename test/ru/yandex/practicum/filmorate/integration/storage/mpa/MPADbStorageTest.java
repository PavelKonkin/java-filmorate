package ru.yandex.practicum.filmorate.integration.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPADbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPADbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAll() {
        MPADbStorage mpaDbStorage = new MPADbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        List<MPA> mpa = mpaDbStorage.findAll();

        assertThat(mpa.size()).isEqualTo(5);
        assertThat(mpa.get(0)).isEqualTo(MPA.builder().id(1).name("G").build());
        assertThat(mpa.get(1)).isEqualTo(MPA.builder().id(2).name("PG").build());
        assertThat(mpa.get(2)).isEqualTo(MPA.builder().id(3).name("PG-13").build());
        assertThat(mpa.get(3)).isEqualTo(MPA.builder().id(4).name("R").build());
        assertThat(mpa.get(4)).isEqualTo(MPA.builder().id(5).name("NC-17").build());
    }

    @Test
    public void testFind() throws NotFoundException {
        MPADbStorage mpaDbStorage = new MPADbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        MPA mpa = mpaDbStorage.find(2);

        assertThat(mpa)
                .isNotNull()
                .isEqualTo(MPA.builder().id(2).name("PG").build());

    }
}
