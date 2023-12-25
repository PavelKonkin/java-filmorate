package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
        log.debug("Запрошен список всех пользователей");
        return List.copyOf(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user != null) {
            User.increaseIdCounter();
            checkUserName(user);
            log.debug("Создан пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Ошибка валидации пользователя {}", user);
            throw new ValidationException();
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (user != null && users.containsKey(user.getId())) {
            checkUserName(user);
            log.debug("Обновлен пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Ошибка валидации пользователя {}", user);
            throw new ValidationException();
        }
    }

    private void checkUserName(User user) {
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
