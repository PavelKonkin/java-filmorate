package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Genre implements Comparable<Genre> {
    private final Integer id;
    private final String name;

    @Override
    public int compareTo(Genre o) {
        return this.id - o.id;
    }
}
