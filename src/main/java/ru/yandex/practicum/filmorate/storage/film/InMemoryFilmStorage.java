package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public int add(Film film) throws ValidationException {
        if (film != null) {
            films.put(film.getId(), film);
            return film.getId();
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void update(Film film) throws NotFoundException, ValidationException {
        if (film == null) {
            throw new ValidationException();
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Фильм с id {} не найден", film.getId());
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer id) throws NotFoundException, ValidationException {
        if (id == null) {
            throw new ValidationException();
        }
        if (films.containsKey(id)) {
            log.debug("Удален фильм {}", films.get(id));
            films.remove(id);
        } else {
            log.debug("Фильм с id {} не найден", id);
            throw new NotFoundException();
        }
    }

    @Override
    public List<Film> findAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film get(int id) throws NotFoundException {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException();
        }
        return films.get(id);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return findAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
