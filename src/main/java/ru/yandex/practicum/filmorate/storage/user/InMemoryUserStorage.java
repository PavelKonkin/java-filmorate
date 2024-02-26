package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public int add(User user) throws ValidationException {
        if (user != null) {
            users.put(user.getId(), user);
            return user.getId();
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
    public void delete(Integer id) throws ValidationException, NotFoundException {
        if (id == null) {
            throw new ValidationException();
        }
        if (users.containsKey(id)) {
            log.debug("Удален пользователь c id {}", id);
            users.remove(id);
        } else {
            log.debug("Не найден пользователь c id {}", id);
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User get(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException();
        }
        return users.get(id);
    }

    @Override
    public List<User> findUserFriends(Integer id) throws ValidationException, NotFoundException {
        Set<Integer> friendsId = get(id).getFriends();
        return findAll().stream()
                .filter(u -> friendsId.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) throws ValidationException, NotFoundException {
        List<User> userFriends = findUserFriends(id);
        List<User> otherUserFriends = findUserFriends(otherId);
        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends;
    }
}
