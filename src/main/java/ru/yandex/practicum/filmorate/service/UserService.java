package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user_friend.UserFriendStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final UserFriendStorage userFriendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
            @Qualifier("userFriendDbStorage") UserFriendStorage userFriendStorage) {
        this.userStorage = userStorage;
        this.userFriendStorage = userFriendStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException();
        }
        User.increaseIdCounter();
        checkUserName(user);
        int id = userStorage.add(user);
        User createdUser = user.toBuilder()
                                           .id(id)
                                           .build();
        log.debug("Создан пользователь {}", createdUser);
        return createdUser;
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
        userFriendStorage.addFriend(id, friendId);
    }

    public void deleteFromFriends(Integer id, Integer friendId) throws NotFoundException, ValidationException {
        userFriendStorage.deleteFriend(id, friendId);
    }

    public List<User> findUserFriends(Integer id) throws NotFoundException, ValidationException {
        return userStorage.findUserFriends(id);
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) throws NotFoundException, ValidationException {
        return userStorage.findCommonFriends(id, otherId);
    }

    public User findUserById(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        return userStorage.get(id);
    }

    public void deleteUserById(Integer id) throws NotFoundException, ValidationException {
        userStorage.delete(id);
    }

    private void checkUserName(User user) {
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
