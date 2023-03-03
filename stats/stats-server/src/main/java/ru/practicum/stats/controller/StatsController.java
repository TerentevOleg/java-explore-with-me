package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.StatsDtoIn;
import ru.practicum.stats.dto.StatsDtoOut;
import ru.practicum.stats.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<StatsDtoIn> saveHit(@RequestBody @Valid StatsDtoIn statsDtoIn) {
        statsService.save(statsDtoIn);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoOut>> get(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        return ResponseEntity.ok().body(statsService.get(start, end, uris, unique));
    }
}
