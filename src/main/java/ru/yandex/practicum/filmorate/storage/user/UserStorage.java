package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    int add(User user) throws ValidationException;

    void update(User user) throws NotFoundException, ValidationException;

    void delete(Integer id) throws ValidationException, NotFoundException;

    List<User> findAll();

    User get(Integer id) throws NotFoundException, ValidationException;

    List<User> findUserFriends(Integer id) throws ValidationException, NotFoundException;

    List<User> findCommonFriends(Integer id, Integer otherId) throws ValidationException, NotFoundException;
}
