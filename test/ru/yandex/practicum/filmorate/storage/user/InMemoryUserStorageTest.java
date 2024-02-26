package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

class InMemoryUserStorageTest extends UserStorageTest<InMemoryUserStorage> {

    @BeforeEach
    void setUp() throws ValidationException {
        userStorage = new InMemoryUserStorage();

        user1 = User.builder()
                .email("user1@mail.com")
                .login("user1")
                .name("user1 name")
                .birthday(LocalDate.now().minusYears(30))
                .id(1)
                .build();

        User.increaseIdCounter();

        user2 = User.builder()
                .email("user2@mail.com")
                .login("user2")
                .name("user2 name")
                .birthday(LocalDate.now().minusYears(29))
                .id(2)
                .build();

        User.increaseIdCounter();

        user3 = User.builder()
                .email("user3@mail.com")
                .login("user3")
                .name("user3 name")
                .birthday(LocalDate.now().minusYears(28))
                .id(3)
                .build();

        user1.getFriends().addAll(List.of(user1.getId(), user3.getId()));
        user3.getFriends().addAll(List.of(user1.getId(), user2.getId()));
        user2.getFriends().addAll(List.of(user3.getId(), user1.getId()));

        User.increaseIdCounter();

        user4 = User.builder()
                .email("user4@mail.com")
                .login("user4")
                .name("user4 name")
                .birthday(LocalDate.now().minusYears(27))
                .id(4)
                .build();

        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);
    }
}