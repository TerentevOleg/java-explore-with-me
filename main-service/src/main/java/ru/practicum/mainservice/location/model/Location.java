package ru.practicum.mainservice.location.model;

import lombok.*;
import ru.practicum.mainservice.category.model.Category;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.nonNull(id) && id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
