package ru.practicum.stats.dto;

import lombok.*;

@Builder
@Value
@RequiredArgsConstructor
public class StatsDtoOut {
    String app;
    String uri;
    Long hits;
}
