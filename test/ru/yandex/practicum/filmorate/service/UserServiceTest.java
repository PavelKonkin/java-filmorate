package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user_friend.InMemoryUserFriendStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;
    User user1;
    User user2;
    User user3;
    User user4;

    @BeforeEach
    void setUp() throws ValidationException {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage, new InMemoryUserFriendStorage(inMemoryUserStorage));

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

        user1.getFriends().addAll(List.of(user2.getId(), user3.getId()));
        user3.getFriends().addAll(List.of(user1.getId(), user2.getId()));
        user2.getFriends().add(user3.getId());

        User.increaseIdCounter();

        user4 = User.builder()
                .email("user4@mail.com")
                .login("user4")
                .name("user4 name")
                .birthday(LocalDate.now().minusYears(27))
                .id(4)
                .build();

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
    }

    @Test
    void shouldFindAll() {
        List<User> users = userService.findAll();
        assertEquals(3, users.size());
        assertTrue(users.containsAll(List.of(user1, user2, user3)));
    }

    @Test
    void shouldCreate() throws ValidationException, NotFoundException {
        userService.create(user4);
        assertEquals(user4, userService.findUserById(user4.getId()));
        assertTrue(userService.findAll().contains(user4));
    }

    @Test
    void shouldThrowExceptionWhenCreateNull() {
        assertThrows(ValidationException.class, () -> userService.create(null));
    }

    @Test
    void shouldUpdate() throws ValidationException, NotFoundException {
        user1.setName("user1 updated");
        userService.update(user1);
        assertEquals(user1.getName(), userService.findUserById(user1.getId()).getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateIncorrectUser() {
        assertThrows(NotFoundException.class, () -> userService.update(user4));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> userService.update(null));
    }

    @Test
    void shouldAddToFriends() throws ValidationException, NotFoundException {
        userService.addToFriends(user2.getId(), user1.getId());
        assertTrue(userService.findUserById(user2.getId()).getFriends().contains(user1.getId()));
    }

    @Test
    void shouldThrowExceptionWhenAddToFriendIncorrectUser() {
        assertThrows(NotFoundException.class, () -> userService.addToFriends(user1.getId(), user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenAddToFriendsNull() {
        assertThrows(ValidationException.class, () -> userService.addToFriends(user1.getId(), null));
    }

    @Test
    void shouldDeleteFromFriends() throws ValidationException, NotFoundException {
        userService.deleteFromFriends(user1.getId(), user2.getId());
        assertFalse(userService.findUserById(user1.getId()).getFriends().contains(user2.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteFromFriendsIncorrectUser() {
        assertThrows(NotFoundException.class,
                () -> userService.deleteFromFriends(user1.getId(), user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteFromFriendsNull() {
        assertThrows(ValidationException.class,
                () -> userService.deleteFromFriends(user1.getId(), null));
    }

    @Test
    void shouldFindUserFriends() throws ValidationException, NotFoundException {
        List<User> friends = userService.findUserFriends(user1.getId());
        assertEquals(2, friends.size());
        assertTrue(friends.stream()
                .map(User::getId)
                .collect(Collectors.toList()).containsAll(List.of(user2.getId(), user3.getId())));
    }

    @Test
    void shouldThrowExceptionWhenFindIncorrectUserFriends() {
        assertThrows(NotFoundException.class, () -> userService.findUserFriends(user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindNullUserFriends() {
        assertThrows(ValidationException.class, () -> userService.findUserFriends(null));
    }

    @Test
    void shouldFindCommonFriends() throws ValidationException, NotFoundException {
        List<User> commonFriends = userService.findCommonFriends(user1.getId(), user3.getId());
        assertEquals(user2, commonFriends.get(0));
        assertEquals(1, commonFriends.size());
    }

    @Test
    void shouldThrowExceptionWhenFindCommonFriendsWithIncorrectUser() {
        assertThrows(NotFoundException.class,
                () -> userService.findCommonFriends(user1.getId(), user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindCommonFriendsWithNull() {
        assertThrows(ValidationException.class,
                () -> userService.findCommonFriends(user1.getId(), null));
    }

    @Test
    void shouldFindUserById() throws ValidationException, NotFoundException {
        User foundUser = userService.findUserById(user1.getId());
        assertEquals(user1, foundUser);
    }

    @Test
    void shouldThrowExceptionWhenFindUserByIncorrectId() {
        assertThrows(NotFoundException.class, () -> userService.deleteUserById(user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindUserByNull() {
        assertThrows(ValidationException.class, () -> userService.deleteUserById(null));
    }

    @Test
    void shouldDeleteUserById() throws ValidationException, NotFoundException {
        userService.deleteUserById(user3.getId());
        assertFalse(userService.findAll().contains(user3));
    }

    @Test
    void shouldThrowExceptionWhenDeleteIncorrectUser() {
        assertThrows(NotFoundException.class, () -> userService.deleteUserById(user4.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNull() {
        assertThrows(ValidationException.class, () -> userService.deleteUserById(null));
    }
}