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

    @Query("SELECT new ru.practicum.stats.dto.StatsDtoOut(ae.name, se.uri, COUNT (se.id)) " +
            "FROM StatEntity se JOIN AppEntity ae ON ae.id=se.app.id " +
            "WHERE se.timestamp BETWEEN :start AND :end " +
            "GROUP BY  se.uri, ae.name " +
            "ORDER BY COUNT(se.id) DESC")
    List<StatsDtoOut> getAllHits(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.StatsDtoOut(ae.name, se.uri, COUNT (se.id))" +
            "FROM StatEntity se JOIN AppEntity ae ON ae.id=se.app.id " +
            "WHERE se.timestamp BETWEEN :start AND :end " +
            "GROUP BY se.uri, se.ip " +
            "ORDER BY COUNT(se.id) DESC")
    List<StatsDtoOut> getAllUniqueHits(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.StatsDtoOut(ae.name, se.uri, COUNT (se.id))" +
            "FROM StatEntity se JOIN AppEntity ae ON ae.id=se.app.id " +
            "WHERE (se.timestamp BETWEEN :start AND :end) AND (se.uri IN (:uris))" +
            "GROUP BY se.uri,ae.name " +
            "ORDER BY COUNT(se.id) DESC")
    List<StatsDtoOut> getHitsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.StatsDtoOut(ae.name, se.uri, count (se.id))" +
            "FROM StatEntity se JOIN AppEntity ae ON ae.id=se.app.id " +
            "WHERE se.timestamp BETWEEN :start AND :end AND (se.uri IN (:uris))" +
            "GROUP BY se.uri, se.ip " +
            "ORDER BY COUNT (se.id) desc")
    List<StatsDtoOut> getUniqueHitsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
