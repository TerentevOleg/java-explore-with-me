package ru.practicum.stats.service;

import ru.practicum.stats.dto.StatsDtoIn;
import ru.practicum.stats.dto.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void save(StatsDtoIn statsDtoIn);
    List<StatsDtoOut> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
