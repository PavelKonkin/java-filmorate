package ru.yandex.practicum.filmorate.storage.user_friend;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

public interface UserFriendStorage {
    void addFriend(Integer userId, Integer friendId) throws NotFoundException, ValidationException;

    void deleteFriend(Integer userId, Integer friendId) throws NotFoundException, ValidationException;
}
