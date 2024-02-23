package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private static final String CINEMA_BIRTHDAY = "1895-12-28";
    private final int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @AfterDate(specificDate = CINEMA_BIRTHDAY,
            message = "Дата релиза должна быть позже даты выхода первого фильма")
    private LocalDate releaseDate;

    @Positive
    @NotNull
    private int duration;

    private static int idCounter = 1;

    private final Set<Integer> likes = new HashSet<>();

    private final TreeSet<Genre> genres;
    private MPA mpa;

    public static void increaseIdCounter() {
        idCounter++;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("description", description);
        values.put("name", name);
        values.put("duration", duration);
        values.put("release_date", releaseDate);
        values.put("rating_id", mpa.getId());

        return values;
    }
}
