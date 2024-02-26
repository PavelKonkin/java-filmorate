package ru.yandex.practicum.filmorate.integration.storage.user_friend;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user_friend.UserFriendDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserFriendDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAddFriend() throws ValidationException, NotFoundException {
        UserFriendDbStorage userFriendDbStorage = new UserFriendDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User newUser1 = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();


        int userId1 = userStorage.add(newUser1);

        User newUser2 = User.builder()
                .email("updated_user@email.ru")
                .name("Ivan Petrov updated")
                .login("vanya123_updated")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        int userId2 = userStorage.add(newUser2);

        // вызываем тестируемый метод

        userFriendDbStorage.addFriend(userId1, userId2);

        String sql = "select * from user_friend where user_id = ? and friend_id = ?";
        int firstUserFriendsCount = jdbcTemplate.queryForList(sql, userId1, userId2).size();
        int secondUserFriendsCount = jdbcTemplate.queryForList(sql, userId2, userId1).size();

        assertThat(firstUserFriendsCount).isEqualTo(1);
        assertThat(secondUserFriendsCount).isEqualTo(0);

    }

    @Test
    public void testDeleteFriend() throws ValidationException, NotFoundException {
        UserFriendDbStorage userFriendDbStorage = new UserFriendDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User newUser1 = User.builder()
                .email("user@email.ru")
                .name("Ivan Petrov")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();


        int userId1 = userStorage.add(newUser1);

        User newUser2 = User.builder()
                .email("updated_user@email.ru")
                .name("Ivan Petrov updated")
                .login("vanya123_updated")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        int userId2 = userStorage.add(newUser2);

        userFriendDbStorage.addFriend(userId1, userId2);

        String sql = "select * from user_friend where user_id = ? and friend_id = ?";
        int firstUserFriendsCount = jdbcTemplate.queryForList(sql, userId1, userId2).size();
        int secondUserFriendsCount = jdbcTemplate.queryForList(sql, userId2, userId1).size();

        assertThat(firstUserFriendsCount).isEqualTo(1);
        assertThat(secondUserFriendsCount).isEqualTo(0);

        // вызываем тестируемый метод
        userFriendDbStorage.deleteFriend(userId1, userId2);
        firstUserFriendsCount = jdbcTemplate.queryForList(sql, userId1, userId2).size();
        assertThat(firstUserFriendsCount).isEqualTo(0);
    }
}
