package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void add(User user) throws ValidationException {
        if (user != null) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void update(User user) throws NotFoundException, ValidationException {
        if (user == null) {
            throw new ValidationException();
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.debug("Не найден пользователь с id {}", user.getId());
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(User user) throws ValidationException, NotFoundException {
        if (user == null) {
            throw new ValidationException();
        }
        if (users.containsKey(user.getId())) {
            log.debug("Удален пользователь {}", user);
            users.remove(user.getId());
        } else {
            log.debug("Не найден пользователь {}", user);
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }
}
