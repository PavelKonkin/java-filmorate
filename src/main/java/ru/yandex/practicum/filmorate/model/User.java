package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private final int id = idCounter;

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    @NoSpaces
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private static int idCounter = 1;
    private final Set<Integer> friends = new HashSet<>();

    public static void increaseIdCounter() {
        idCounter++;
    }
}
