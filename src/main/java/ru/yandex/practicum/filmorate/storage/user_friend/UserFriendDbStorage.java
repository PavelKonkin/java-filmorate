package ru.yandex.practicum.filmorate.storage.user_friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component
@Slf4j
@Qualifier("userFriendDbStorage")
public class UserFriendDbStorage implements UserFriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) throws NotFoundException {
        String sql = "insert into user_friend (user_id, friend_id) values (?, ?)";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DataAccessException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws NotFoundException {
        String sql = "delete from user_friend where user_id = ? and friend_id = ?";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DataAccessException e) {
            throw new NotFoundException();
        }
    }
}
