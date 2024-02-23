package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create(Genre genre) {
        return 0;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql,(rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre find(int id) throws NotFoundException {
        String sql = "select * from genre where id = ?";

        try {
            Genre genre = jdbcTemplate.queryForObject(sql,(rs, rowNum) -> makeGenre(rs), id);
            log.debug("Жанр {} найден", genre);
            return genre;
        } catch (DataAccessException e) {
            log.debug("Жанр с id {} не найден", id);
            throw new NotFoundException();
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int id = rs.getInt("id");
        return Genre.builder()
                .name(name)
                .id(id)
                .build();
    }
}
