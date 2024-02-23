package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int add(User user) throws ValidationException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public void update(User user) throws NotFoundException, ValidationException {
        String sql = "update USERS set name = ?, email = ?, login = ?, birthday = ? where id = ?";
        int updatedQuantity = jdbcTemplate.update(sql,
                            user.getName(),
                            user.getEmail(),
                            user.getLogin(),
                            user.getBirthday(),
                            user.getId());
        if (updatedQuantity == 0) {
            log.debug("Не найден пользователь с id {}", user.getId());
            throw new NotFoundException();
        }

    }

    @Override
    public void delete(Integer id) throws ValidationException, NotFoundException {
        String sql = "delete from USERS where id = ?";
        int deletedQuantity = jdbcTemplate.update(sql, id);
        if (deletedQuantity == 0) {
            log.debug("Не найден пользователь с id {}", id);
            throw new NotFoundException();
        } else {
            log.debug("Удален пользователь с id {}", id);
        }

    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User get(Integer id) throws NotFoundException {
        String sql = "select * from users where ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
        } catch (DataAccessException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> findUserFriends(Integer id) {
        String sql = "select * from users where id in (select friend_id from user_friend where user_id = ?)";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        String sql = "select * from users where id in (select friend_id from user_friend where user_id = ?) " +
                "and id in (select friend_id from user_friend where user_id = ?)";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    private User makeUser(ResultSet rs) throws SQLException {

        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        int id = rs.getInt("id");
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .id(id)
                .build();
    }
}
