package ru.yandex.practicum.filmorate.storage.film_genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FilmGenreDbStorage implements FilmGenre {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    @Override
    public void create(int filmId, List<Integer> genreIds) {
        if (!genreIds.isEmpty()) {
            String sql = "merge into film_genre (film_id, genre_id) values (:film_id, :genre_id)";
            namedParameterJdbcTemplate.batchUpdate(sql, genreIds.stream()
                    .map(genreId -> new MapSqlParameterSource()
                            .addValue("film_id", filmId)
                            .addValue("genre_id", genreId)).toArray(MapSqlParameterSource[]::new));
        }
    }

    @Override
    public void update(int filmId, List<Integer> genreIds) {
        delete(filmId);
        create(filmId, genreIds);
    }

    private void delete(int filmId) {
        String sql = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
