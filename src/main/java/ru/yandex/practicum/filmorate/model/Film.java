package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterCinemaBirthday;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private static final String CINEMA_BIRTHDAY = "1895-12-28";
    private final int id = idCounter;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @AfterCinemaBirthday(specificDate = CINEMA_BIRTHDAY,
            message = "Дата релиза должна быть позже даты выхода первого фильма")
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private int duration;
    private static int idCounter = 1;

    public static void increaseIdCounter() {
        idCounter++;
    }

}
