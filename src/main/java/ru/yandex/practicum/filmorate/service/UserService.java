package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        updateFriendship(id, friendId, FriendshipAction.ADD_TO_FRIENDS);
    }

    public void deleteFromFriends(Integer id, Integer friendId) throws NotFoundException, ValidationException {
        updateFriendship(id, friendId, FriendshipAction.REMOVE_FROM_FRIENDS);
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

    private void checkUserName(User user) {
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void updateFriendship(Integer id, Integer otherId, FriendshipAction action) throws NotFoundException, ValidationException {
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = otherUser.getFriends();
        if (action == FriendshipAction.ADD_TO_FRIENDS) {
            userFriends.add(otherId);
            otherUserFriends.add(id);
        } else if (action == FriendshipAction.REMOVE_FROM_FRIENDS) {
            userFriends.remove(otherId);
            otherUserFriends.remove(id);
        }
        userStorage.update(user);
        userStorage.update(otherUser);
    }

    private enum FriendshipAction {
        ADD_TO_FRIENDS,
        REMOVE_FROM_FRIENDS
    }
}
