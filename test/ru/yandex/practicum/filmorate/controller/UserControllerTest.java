package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user_friend.InMemoryUserFriendStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userControllerWithUsers;
    UserController userControllerWithoutUsers;
    User user1;
    User user2;
    User user3;
    User invalidLoginUser;
    User invalidEmailUser;
    User invalidBirthDayUser;
    User emptyNameUser;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void initializeTestData() throws ValidationException, NotFoundException {
        InMemoryUserStorage inMemoryUserStorage1 = new InMemoryUserStorage();
        InMemoryUserStorage inMemoryUserStorage2 = new InMemoryUserStorage();
        userControllerWithUsers = new UserController(new UserService(inMemoryUserStorage1
                , new InMemoryUserFriendStorage(inMemoryUserStorage1)));
        userControllerWithoutUsers = new UserController(new UserService(inMemoryUserStorage2
                , new InMemoryUserFriendStorage(inMemoryUserStorage2)));

        user1 = User.builder()
                .email("user1@mail.com")
                .login("user1")
                .name("user1")
                .birthday(LocalDate.now().minusYears(30))
                .id(1)
                .build();
        userControllerWithUsers.create(user1);

        user2 = User.builder()
                .email("user2@mail.com")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.now().minusYears(30))
                .id(2)
                .build();
        userControllerWithUsers.create(user2);

        userControllerWithUsers.addToFriends(user2.getId(), user1.getId());

        user3 = User.builder()
                .email("user3@mail.com")
                .login("user3")
                .name("user3")
                .birthday(LocalDate.now().minusYears(30))
                .id(3)
                .build();

        invalidLoginUser = User.builder()
                .email("user2@mail.com")
                .login("user 2")
                .name("user2")
                .birthday(LocalDate.now().minusYears(30))
                .id(4)
                .build();

        invalidEmailUser = User.builder()
                .email("mail.com")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.now().minusYears(30))
                .id(5)
                .build();

        invalidBirthDayUser = User.builder()
                .email("mail@mail.ru")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.now().plusYears(100))
                .id(6)
                .build();

        emptyNameUser = User.builder()
                .email("mail@mail.ru")
                .login("user2")
                .birthday(LocalDate.now().plusYears(100))
                .id(7)
                .build();
        emptyNameUser.setLogin("user2");
        emptyNameUser.setBirthday(LocalDate.now().minusYears(30));
        emptyNameUser.setEmail("mail@mail.ru");

    }

    @Test
    void shouldReturnEmptyList() {
        assertEquals(List.of(), userControllerWithoutUsers.findAll());
    }

    @Test
    void shouldReturnUsersList() {
        assertEquals(2, userControllerWithUsers.findAll().size());
    }

    @Test
    void shouldFindUser() throws ValidationException, NotFoundException {
        assertEquals(user1, userControllerWithUsers.findUser(user1.getId()));
    }

    @Test
    void shouldThrowExceptionWhenFindIncorrectId() {
        assertThrows(NotFoundException.class, () -> userControllerWithUsers.findUser(9999));
    }

    @Test
    void shouldThrowExceptionWhenFindNull() {
        assertThrows(ValidationException.class, () -> userControllerWithUsers.findUser(null));
    }

    @Test
    void shouldThrowExceptionWhenCreateNull() {
        assertThrows(ValidationException.class, () -> userControllerWithUsers.create(null));
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidLoginUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidLoginUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidEmailUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidEmailUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenCreateInvalidBirthDayUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(invalidBirthDayUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> userControllerWithUsers.update(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidLoginUser() {
        user1.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(invalidLoginUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidEmailUser() {
        user1.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldThrowExceptionWhenUpdateInvalidBirthDayUser() {
        user1.setBirthday(LocalDate.now().plusYears(100));
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldCreateUser() throws ValidationException {
        User userCreated = userControllerWithUsers.create(user3);
        assertEquals(user3, userCreated);
    }

    @Test
    void shouldUpdateUser() throws ValidationException, NotFoundException {
        user2.setEmail("new_mail@mail.ru");
        userControllerWithUsers.update(user2);
        assertEquals(Objects.requireNonNull(userControllerWithUsers.findAll().stream()
                .filter(user -> user.getId() == user2.getId())
                .findFirst()
                .orElse(null)).getEmail(), user2.getEmail());
    }

    @Test
    void shouldAddToFriends() throws ValidationException, NotFoundException {
        userControllerWithUsers.addToFriends(user1.getId(), user2.getId());
        User userWithFriend = userControllerWithUsers.findUser(user1.getId());
        assertEquals(1, userWithFriend.getFriends().size());
        assertTrue(userWithFriend.getFriends().contains(user2.getId()));
    }

    @Test
    void shouldThrowExceptionWhenAddToFriendsWithIncorrectId() {
        assertThrows(NotFoundException.class, () -> userControllerWithUsers.addToFriends(999, 1));
    }

    @Test
    void shouldThrowExceptionWhenAddToFriendsWithNull() {
        assertThrows(ValidationException.class,
                () -> userControllerWithUsers.addToFriends(null, null));
    }

    @Test
    void shouldDeleteFromFriends() throws NotFoundException, ValidationException {
        userControllerWithUsers.deleteFromFriends(user2.getId(), user1.getId());
        assertEquals(0, userControllerWithUsers.findUser(user2.getId()).getFriends().size());
    }

    @Test
    void shouldThrowExceptionWhenDeleteFromFriendsIncorrectId() {
        assertThrows(NotFoundException.class,
                () -> userControllerWithUsers.deleteFromFriends(999, 9));
    }

    @Test
    void shouldThrowExceptionWhenDeleteFromFriendNull() {
        assertThrows(ValidationException.class,
                () -> userControllerWithUsers.deleteFromFriends(null, null));
    }

    @Test
    void shouldFindUserFriends() throws ValidationException, NotFoundException {
        assertEquals(1, userControllerWithUsers.findUserFriends(user2.getId()).size());
        assertEquals(user1, userControllerWithUsers.findUserFriends(user2.getId()).get(0));
    }

    @Test
    void shouldThrowExceptionWhenFindIncorrectUserFriends() {
        assertThrows(NotFoundException.class, () -> userControllerWithUsers.findUserFriends(999));
    }

    @Test
    void shouldThrowExceptionWhenFindNullUserFriends() {
        assertThrows(ValidationException.class, () -> userControllerWithUsers.findUserFriends(null));
    }

    @Test
    void shouldFindCommonFriends() throws ValidationException, NotFoundException {
        userControllerWithUsers.create(user3);
        userControllerWithUsers.addToFriends(user3.getId(), user1.getId());
        assertEquals(1,
                userControllerWithUsers.findCommonFriends(user2.getId(), user3.getId()).size());
        assertEquals(user1, userControllerWithUsers.findCommonFriends(user2.getId(), user3.getId()).get(0));
    }

    @Test
    void shouldThrowExceptionWhenFindCommonFriendsWithIncorrectId() {
        assertThrows(NotFoundException.class,
                () -> userControllerWithUsers.findCommonFriends(666, 999));
    }

    @Test
    void shouldThrowExceptionWhenFindCommonFriendsWithNull() {
        assertThrows(ValidationException.class,
                () -> userControllerWithUsers.findCommonFriends(null, null));
    }

    @Test
    void shouldDelete() throws ValidationException, NotFoundException {
        userControllerWithUsers.deleteUser(user1.getId());
        assertEquals(1, userControllerWithUsers.findAll().size());
        assertFalse(userControllerWithUsers.findAll().contains(user1));
    }

}