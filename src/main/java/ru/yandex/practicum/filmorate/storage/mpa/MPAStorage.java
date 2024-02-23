package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {
    int create(MPA mpa);

    List<MPA> findAll();

    MPA find(int id) throws NotFoundException;
}
