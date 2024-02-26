package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.yandex.practicum.filmorate.validator.AfterDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Entity
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Film {
    private static final String CINEMA_BIRTHDAY = "1895-12-28";
    @Id
    private final Integer id;

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

    @Transient
    private final Set<Integer> likes = new HashSet<>();
    @Transient
    @ToString.Exclude
    private final TreeSet<Genre> genres;
    @Transient
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Film film = (Film) o;
        return getId() != null && Objects.equals(getId(), film.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
