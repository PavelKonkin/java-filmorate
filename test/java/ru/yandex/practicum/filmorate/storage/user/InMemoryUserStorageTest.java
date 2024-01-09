package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest extends UserStorageTest<InMemoryUserStorage> {

    @BeforeEach
    void setUp() throws ValidationException {
        userStorage = new InMemoryUserStorage();

        user1 = new User();
        user1.setEmail("user1@mail.com");
        user1.setLogin("user1");
        user1.setName("user1 name");
        user1.setBirthday(LocalDate.now().minusYears(30));

        User.increaseIdCounter();

        user2 = new User();
        user2.setEmail("user2@mail.com");
        user2.setLogin("user2");
        user2.setName("user2 name");
        user2.setBirthday(LocalDate.now().minusYears(29));

        User.increaseIdCounter();

        user3 = new User();
        user3.setEmail("user3@mail.com");
        user3.setLogin("user3");
        user3.setName("user3 name");
        user3.setBirthday(LocalDate.now().minusYears(28));

        user1.getFriends().addAll(List.of(user1.getId(), user3.getId()));
        user3.getFriends().addAll(List.of(user1.getId(), user2.getId()));
        user2.getFriends().addAll(List.of(user3.getId(), user1.getId()));

        User.increaseIdCounter();

        user4 = new User();
        user4.setEmail("user4@mail.com");
        user4.setLogin("user4");
        user4.setName("user4 name");
        user4.setBirthday(LocalDate.now().minusYears(27));

        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);
    }
}