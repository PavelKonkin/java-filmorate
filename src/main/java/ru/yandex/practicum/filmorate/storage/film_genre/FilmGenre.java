package ru.yandex.practicum.filmorate.storage.film_genre;

import java.util.List;

public interface FilmGenre {
    void create(int filmId, List<Integer> genreIds);

    void update(int filmId, List<Integer> genreIds);
}
