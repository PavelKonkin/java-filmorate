package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    int create(Genre genre);

    List<Genre> findAll();

    Genre find(int id) throws NotFoundException;
}
