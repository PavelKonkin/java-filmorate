package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void add(User user) throws ValidationException;

    void update(User user) throws NotFoundException, ValidationException;

    void delete(User user) throws ValidationException, NotFoundException;

    List<User> findAll();
}
