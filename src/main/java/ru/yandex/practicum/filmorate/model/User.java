package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    @Id
    private final Integer id;

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
    @Transient
    private final Set<Integer> friends = new HashSet<>();
    @Transient
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
