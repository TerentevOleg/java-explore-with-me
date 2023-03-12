package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.dto.EventDtoOut;
import ru.practicum.mainservice.event.dto.EventShortDtoOut;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.stats.dto.StatsDtoIn;
import ru.practicum.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventPublicController {

    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDtoOut>> getEventsByFilters(@RequestParam(required = false) String text,
                                                                       @RequestParam (required = false) List<Long> categories,
                                                                       @RequestParam (required = false) Boolean paid,
                                                                       @RequestParam (required = false) String rangeStart,
                                                                       @RequestParam (required = false) String rangeEnd,
                                                                       @RequestParam (required = false) Boolean onlyAvailable,
                                                                       @RequestParam (required = false) String sort,
                                                                       @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                                       @RequestParam (name = "size", defaultValue = "10") Integer size,
                                                                       HttpServletRequest httpServletRequest) {
        StatsDtoIn statsDtoIn = StatsDtoIn.builder()
                .app("ewm-main")
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.hit(statsDtoIn);
        return ResponseEntity.ok(eventService.getEventsByFilters(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDtoOut> get(@PathVariable Long id,
                                           HttpServletRequest httpServletRequest) {
        StatsDtoIn statsDtoIn = StatsDtoIn.builder()
                .app("ewm-main")
                .uri("/events/" + id)
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.hit(statsDtoIn);
        return ResponseEntity.ok(eventService.getById(id));
    }
}
