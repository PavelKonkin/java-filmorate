package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Запрошен список всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Validated
    public User findUser(@PathVariable @NotNull Integer id) throws NotFoundException, ValidationException {
        return userService.findUserById(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user != null) {
            userService.create(user);
            return user;
        } else {
            log.debug("Ошибка валидации пользователя: пришел null");
            throw new ValidationException();
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException, NotFoundException {
        if (user != null) {
            userService.update(user);
            return user;
        } else {
            log.debug("Ошибка валидации пользователя: пришел null");
            throw new ValidationException();
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    @Validated
    public void addToFriends(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer friendId)
            throws NotFoundException, ValidationException {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @Validated
    public void deleteFromFriends(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer friendId)
            throws NotFoundException, ValidationException {
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @Validated
    public List<User> findUserFriends(@PathVariable @NotNull Integer id) throws NotFoundException, ValidationException {
        return userService.findUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Validated
    public List<User> findCommonFriends(@PathVariable @NotNull Integer id,
                                        @PathVariable @NotNull Integer otherId)
            throws NotFoundException, ValidationException {
        return userService.findCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    @Validated
    public void deleteUser(@PathVariable @NotNull Integer id) throws ValidationException, NotFoundException {
        if (id != null) {
            userService.deleteUserById(id);
        } else {
            throw new ValidationException();
        }
    }
}
