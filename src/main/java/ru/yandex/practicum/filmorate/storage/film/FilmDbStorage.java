package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDbStorage filmGenreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreDbStorage filmGenreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDbStorage = filmGenreDbStorage;
    }

    @Override
    public int add(Film film) throws ValidationException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int createdFilmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        filmGenreDbStorage.create(createdFilmId, getGenreIds(film));
        return createdFilmId;
    }

    @Override
    public void update(Film film) throws NotFoundException, ValidationException {
        String sql = "update FILMS set NAME = ?, DESCRIPTION = ?, DURATION = ?" +
                ", RELEASE_DATE = ?, RATING_ID = ? where ID = ?";
        int updatedQuantity = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        if (updatedQuantity == 0) {
            log.debug("Не найден фильм с id {}", film.getId());
            throw new NotFoundException();
        } else {
            filmGenreDbStorage.update(film.getId(), getGenreIds(film));
        }
    }

    @Override
    public void delete(Integer id) throws NotFoundException, ValidationException {
        String sql = "delete from films where id = ?";
        int deletedQuantity = jdbcTemplate.update(sql, id);
        if (deletedQuantity == 0) {
            log.debug("Не найден фильм с id {}", id);
            throw new NotFoundException();
        } else {
            log.debug("Удален фильм с id {}", id);
        }
    }

    @Override
    public List<Film> findAll() {
        String sql = "select f.ID AS id," +
                " f.NAME AS name," +
                " f.DESCRIPTION AS description," +
                " f.RELEASE_DATE AS release_date," +
                " f.DURATION AS duration," +
                " f.rating_id AS rating_id, " +
                " MPA.name AS mpa_name, " +
                " GROUP_CONCAT(fg.GENRE_ID) AS genre_ids," +
                " GROUP_CONCAT(g.name) AS genre_names" +
                " from FILMS as f " +
                " left join FILM_GENRE as fg on f.id = fg.film_id " +
                " left join MPA on f.rating_id = MPA.id" +
                " left join GENRE AS g on fg.genre_id = g.id" +
                " GROUP BY id;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film get(int id) throws NotFoundException {
        String sql = "select f.ID AS id," +
                " f.NAME AS name," +
                " f.DESCRIPTION AS description," +
                " f.RELEASE_DATE AS release_date," +
                " f.DURATION AS duration," +
                " f.rating_id AS rating_id," +
                " MPA.name AS mpa_name, " +
                " GROUP_CONCAT(fg.GENRE_ID) AS genre_ids," +
                " GROUP_CONCAT(g.name) AS genre_names" +
                " from FILMS as f " +
                " left join FILM_GENRE as fg on f.id = fg.film_id" +
                " left join MPA on f.rating_id = MPA.id" +
                " left join GENRE AS g on fg.genre_id = g.id" +
                " where f.id = ?" +
                " GROUP BY id;";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
        } catch (DataAccessException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sql = "select f.ID AS id," +
                " f.NAME AS name," +
                " f.DESCRIPTION AS description," +
                " f.RELEASE_DATE AS release_date," +
                " f.DURATION AS duration," +
                " f.rating_id AS rating_id," +
                " MPA.name AS mpa_name, " +
                " GROUP_CONCAT(fg.GENRE_ID) AS genre_ids," +
                " GROUP_CONCAT(g.name) AS genre_names," +
                " count(fl.user_id) AS likes" +
                " from FILMS as f " +
                " left join FILM_GENRE as fg on f.id = fg.film_id " +
                " left join film_like as fl on f.id = fl.film_id" +
                " left join MPA on f.rating_id = MPA.id" +
                " left join GENRE AS g on fg.genre_id = g.id" +
                " GROUP BY id" +
                " ORDER BY likes DESC" +
                " LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {

        String description = rs.getString("description");
        String name = rs.getString("name");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int id = rs.getInt("id");
        MPA mpa = MPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("mpa_name"))
                .build();
        String rsGenresId = rs.getString("genre_ids");
        String rsGenresName = rs.getString("genre_names");
        TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        if (rsGenresId != null) {
            String[] genresId = rsGenresId.split(",");
            String[] genresName = rsGenresName.split(",");
            for (int i = 0; i < genresId.length; i++) {
                Genre genre = Genre.builder()
                        .id(Integer.parseInt(genresId[i]))
                        .name(genresName[i])
                        .build();
                genres.add(genre);
            }
        }
        return Film.builder()
                .description(description)
                .name(name)
                .duration(duration)
                .releaseDate(releaseDate)
                .id(id)
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private List<Integer> getGenreIds(Film film) {
        Set<Genre> genres = film.getGenres();
        List<Integer> genreIds = new ArrayList<>();
        if (genres != null) {
            for (Genre genre : genres) {
                genreIds.add(genre.getId());
            }
        }
        return genreIds;
    }
}
