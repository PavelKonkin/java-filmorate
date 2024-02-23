package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class UserStorageTest<T extends UserStorage> {
    T userStorage;
    User user1;
    User user2;
    User user3;
    User user4;

    @Test
    void shouldAdd() throws ValidationException {
        userStorage.add(user4);
        assertEquals(4, userStorage.findAll().size());
        assertTrue(userStorage.findAll().contains(user4));
    }

    @Test
    void shouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> userStorage.add(null));
    }

    @Test
    void shouldUpdate() throws NotFoundException, ValidationException {
        user1.setName("user1 updated name");
        userStorage.update(user1);
        assertEquals(user1.getName(), userStorage.findAll().stream()
                .filter(u -> u.getId() == user1.getId())
                .findFirst()
                .orElseThrow()
                .getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidUser() {
        assertThrows(NotFoundException.class, () -> userStorage.update(user4));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> userStorage.update(null));
    }

    @Test
    void shouldDelete() throws ValidationException, NotFoundException {
        userStorage.delete(user3.getId());
        assertEquals(2, userStorage.findAll().size());
        assertFalse(userStorage.findAll().contains(user3));
    }

    @Test
    void shouldThrowExceptionWhenDeleteInvalidUser() {
        assertThrows(NotFoundException.class, () -> userStorage.delete(user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNull() {
        assertThrows(ValidationException.class, () -> userStorage.delete(null));
    }

    @Test
    void shouldFindAll() {
        List<User> users = userStorage.findAll();
        assertEquals(3, users.size());
        assertTrue(users.containsAll(List.of(user1, user2, user3)));
    }
}