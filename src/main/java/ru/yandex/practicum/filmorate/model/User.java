package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private final int id;

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
    private final Map<Integer, FriendshipStatus> friendshipStatus = new HashMap<>();

    public static void increaseIdCounter() {
        idCounter++;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("name", name);
        values.put("login", login);
        values.put("birthday", birthday);

        return values;
    }
}
