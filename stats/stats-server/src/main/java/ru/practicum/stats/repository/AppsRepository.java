package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.AppEntity;

import java.util.Optional;

@Repository
public interface AppsRepository extends JpaRepository<AppEntity, Long> {
    Optional<AppEntity> findByName(String name);
}
