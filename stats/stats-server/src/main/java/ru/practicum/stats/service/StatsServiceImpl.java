package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.StatsDtoIn;
import ru.practicum.stats.dto.StatsDtoOut;
import ru.practicum.stats.mapper.StatMapper;
import ru.practicum.stats.model.AppEntity;
import ru.practicum.stats.model.StatEntity;
import ru.practicum.stats.repository.AppsRepository;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final AppsRepository appsRepository;
    private final StatMapper statMapper;

    @Override
    @Transactional
    public void save(StatsDtoIn statDtoIn) {
        String appName = statDtoIn.getApp();
        AppEntity app = appsRepository.findByName(appName)
                .orElseGet(() -> {
                    AppEntity newApp = new AppEntity();
                    newApp.setName(appName);
                    return appsRepository.save(newApp);
                });

        StatEntity statEntity = statMapper.fromDto(statDtoIn, app);
        statsRepository.save(statEntity);
        log.debug("Save statistics hit: {}", statEntity);
    }

    @Override
    public List<StatsDtoOut> get(LocalDateTime start, LocalDateTime end,
                                      List<String> uris, boolean unique) {
        List<StatsDtoOut> stats;
            if (unique) {
                stats = statsRepository.findByTimeUnique(start, end, uris);
            } else {
                stats = statsRepository.findByTime(start, end, uris);
            }
        return stats;
    }
}