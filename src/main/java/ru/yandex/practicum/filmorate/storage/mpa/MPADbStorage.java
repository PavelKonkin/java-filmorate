package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> findAll() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql,(rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public MPA find(int id) throws NotFoundException {
        String sql = "select * from mpa where id = ?";

        try {
            MPA mpa = jdbcTemplate.queryForObject(sql,(rs, rowNum) -> makeMPA(rs), id);
            log.debug("Рейтинг {} найден", mpa);
            return mpa;
        } catch (DataAccessException e) {
            log.debug("Рейтинг с id {} не найден", id);
            throw new NotFoundException();
        }
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int id = rs.getInt("id");
        return MPA.builder()
                .name(name)
                .id(id)
                .build();
    }
}
