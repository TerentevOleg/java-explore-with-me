package ru.practicum.mainservice.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
