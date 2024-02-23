package ru.yandex.practicum.filmorate.storage.film_like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component
@Slf4j
@Qualifier("filmLikeDbStorage")
public class FilmLikeDbStorage implements FilmLikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Integer filmId, Integer userId) {
        String sql = "insert into film_like (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

    }

    @Override
    public void delete(Integer filmId, Integer userId) throws NotFoundException {
        String sql = "delete from film_like where film_id = ? and user_id = ?";
        int count = jdbcTemplate.update(sql, filmId, userId);
        if (count == 0) {
            throw new NotFoundException();
        }
    }
}
