package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.info("Запрошен список всех пользователей");
        return List.copyOf(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (validate(user)) {
            if ((user.getName() == null) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.info("Создан пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            User.setBackIdCounter();
            log.info("Ошибка валидации пользователя {}", user);
            throw new ValidationException();
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        User.setBackIdCounter();
        if (validate(user) && users.containsKey(user.getId())) {
            if ((user.getName() == null) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.info("Обновлен пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.info("Ошибка валидации пользователя {}", user);
            throw new ValidationException();
        }
    }

    private boolean validate(User user) {
        if (user == null) {
            return false;
        }
        boolean isEmailValid = user.getEmail() != null && !user.getEmail().isBlank()
                && user.getEmail().contains("@");
        boolean isLoginValid = user.getLogin() != null
                && !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        boolean isBirthdayValid = user.getBirthday() != null
                && user.getBirthday().isBefore(LocalDate.now());
        return isEmailValid && isLoginValid && isBirthdayValid;
    }
}
