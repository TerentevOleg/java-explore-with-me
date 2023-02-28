package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.dto.StatsDtoOut;
import ru.practicum.stats.model.StatEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<StatEntity, Long> {

    @Query("select s from StatEntity s where s.timestamp between ?1 and ?2 and s.uri in ?3")
    List<StatsDtoOut> findByTime(LocalDateTime startTime, LocalDateTime endTime, List<String> uris);

    @Query("select distinct s from StatEntity s where s.timestamp between ?1 and ?2 and s.uri in ?3")
    List<StatsDtoOut> findByTimeUnique(LocalDateTime startTime, LocalDateTime endTime, List<String> uris);
}
