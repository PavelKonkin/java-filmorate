package ru.yandex.practicum.filmorate.integration.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        int userId = userStorage.add(newUser);
        // вызываем тестируемый метод
        User savedUser = userStorage.get(userId);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testCreateUser() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        // вызываем тестируемый метод
        int userId = userStorage.add(newUser);


        User savedUser = userStorage.get(userId);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                // проверяем, что объект не равен null
                .usingRecursiveComparison()
                .ignoringFields("id")// проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testDeleteUser() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        int userId = userStorage.add(newUser);

        // вызываем тестируемый метод
        userStorage.delete(userId);

        // проверяем утверждения
        assertThat(newUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isNotIn(userStorage.findAll());
    }

    @Test
    public void testUpdateUser() throws ValidationException, NotFoundException {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        int userId = userStorage.add(newUser);

        User updatedUser = User.builder()
                .email("updated_user@email.ru")
                .name("Ivan Petrov updated")
                .login("vanya123_updated")
                .birthday(LocalDate.of(1990, 1, 1))
                .id(userId)
                .build();

        // вызываем тестируемый метод
        userStorage.update(updatedUser);

        // проверяем утверждения
        assertThat(userStorage.get(userId))
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    public void testFindAll() throws ValidationException {
        // Подготавливаем данные для теста
        User newUser1 = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();



        User newUser2 = User.builder()
                .email("user2@email.ru")
                .name("Ivan Petrov 2")
                .login("vanya2")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.add(newUser1);
        userStorage.add(newUser2);


        // проверяем утверждения
        assertThat(userStorage.findAll())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(newUser1, newUser2));
    }
}