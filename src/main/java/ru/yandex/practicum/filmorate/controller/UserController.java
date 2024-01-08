package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Запрошен список всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Integer id) throws NotFoundException, ValidationException {
        if (id != null) {
            return userService.findUserById(id);
        } else {
            throw new ValidationException();
        }
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
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId)
            throws NotFoundException, ValidationException {
        if (id != null && friendId != null) {
            userService.addToFriends(id, friendId);
        } else {
            throw new ValidationException();
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Integer id, @PathVariable Integer friendId)
            throws NotFoundException, ValidationException {
        if (id != null && friendId != null) {
            userService.deleteFromFriends(id, friendId);
        } else {
            throw new ValidationException();
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable Integer id) throws NotFoundException, ValidationException {
        if (id != null) {
            return userService.findUserFriends(id);
        } else {
            throw new ValidationException();
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId)
            throws NotFoundException, ValidationException {
        if (id != null && otherId != null) {
            return userService.findCommonFriends(id, otherId);
        } else {
            throw new ValidationException();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) throws ValidationException, NotFoundException {
        if (id != null) {
            userService.deleteUserById(id);
        } else {
            throw new ValidationException();
        }
    }
}
