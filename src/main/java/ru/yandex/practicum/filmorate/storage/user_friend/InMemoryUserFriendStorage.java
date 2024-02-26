package ru.yandex.practicum.filmorate.storage.user_friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

@Component
@Slf4j
public class InMemoryUserFriendStorage implements UserFriendStorage {
    private final UserStorage userStorage;

    public InMemoryUserFriendStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) throws NotFoundException, ValidationException {
        updateFriendship(userId, friendId, FriendshipAction.ADD_TO_FRIENDS);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws NotFoundException, ValidationException {
        updateFriendship(userId, friendId, FriendshipAction.REMOVE_FROM_FRIENDS);
    }

    private void updateFriendship(Integer id, Integer otherId, FriendshipAction action) throws NotFoundException, ValidationException {
        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);
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
