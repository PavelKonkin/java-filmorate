package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void initializeTestData() throws ValidationException {
        userControllerWithUsers = new UserController();
        userControllerWithoutUsers = new UserController();

        user1 = new User();
        user1.setName("user1");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.now().minusYears(30));
        user1.setEmail("user1@mail.ru");
        userControllerWithUsers.create(user1);

        user2 = new User();
        user2.setName("user2");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.now().minusYears(30));
        user2.setEmail("user2@mail.ru");
        userControllerWithUsers.create(user2);

        user3 = new User();
        user3.setName("user3");
        user3.setLogin("user3");
        user3.setBirthday(LocalDate.now().minusYears(30));
        user3.setEmail("user3@mail.ru");

        invalidLoginUser = new User();
        invalidLoginUser.setName("user2");
        invalidLoginUser.setLogin("user 2");
        invalidLoginUser.setBirthday(LocalDate.now().minusYears(30));
        invalidLoginUser.setEmail("user2@mail.ru");

        invalidEmailUser = new User();
        invalidEmailUser.setName("user2");
        invalidEmailUser.setLogin("user2");
        invalidEmailUser.setBirthday(LocalDate.now().minusYears(30));
        invalidEmailUser.setEmail("mail.ru");

        invalidBirthDayUser = new User();
        invalidBirthDayUser.setName("user2");
        invalidBirthDayUser.setLogin("user2");
        invalidBirthDayUser.setBirthday(LocalDate.now().plusYears(100));
        invalidBirthDayUser.setEmail("mail@mail.ru");

        emptyNameUser = new User();
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
    void shouldUpdateUser() throws ValidationException {
        user2.setEmail("new_mail@mail.ru");
        userControllerWithUsers.update(user2);
        assertEquals(Objects.requireNonNull(userControllerWithUsers.findAll().stream()
                .filter(user -> user.getId() == user2.getId())
                .findFirst()
                .orElse(null)).getEmail(), user2.getEmail());
    }
}