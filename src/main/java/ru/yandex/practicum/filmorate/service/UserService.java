package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void create(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException();
        }
        User.increaseIdCounter();
        checkUserName(user);
        userStorage.add(user);
        log.debug("Создан пользователь {}", user);
    }

    public void update(User user) throws NotFoundException, ValidationException {
        if (user == null) {
            throw new ValidationException();
        }
        checkUserName(user);
        userStorage.update(user);
        log.debug("Обновлен пользователь {}", user);
    }

    public void addToFriends(Integer id, Integer friendId) throws NotFoundException, ValidationException {
        addToFriend(id, friendId);
        addToFriend(friendId, id);
    }

    public void deleteFromFriends(Integer id, Integer friendId) throws NotFoundException, ValidationException {
        removeFromFriends(id, friendId);
        removeFromFriends(friendId, id);
    }

    public List<User> findUserFriends(Integer id) throws NotFoundException, ValidationException {
        Set<Integer> friendsId = findUserById(id).getFriends();
        return findAll().stream()
                .filter(u -> friendsId.contains(u.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) throws NotFoundException, ValidationException {
        List<User> userFriends = findUserFriends(id);
        List<User> otherUserFriends = findUserFriends(otherId);
        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends;
    }

    private void checkUserName(User user) {
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User findUserById(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        return userStorage.findAll().stream()
                .filter(u -> u.getId() == id)
                .findAny().orElseThrow(NotFoundException::new);
    }

    public void deleteUserById(Integer id) throws NotFoundException, ValidationException {
        userStorage.delete(findUserById(id));
    }

    private void addToFriend(Integer id, Integer otherId) throws NotFoundException, ValidationException {
        User user = findUserById(id);
        Set<Integer> userFriends = user.getFriends();
        userFriends.add(otherId);
        userStorage.update(user);
    }

    private void removeFromFriends(Integer id, Integer otherId) throws NotFoundException, ValidationException {
        User user = findUserById(id);
        Set<Integer> userFriends = user.getFriends();
        userFriends.remove(otherId);
        userStorage.update(user);
    }
}
