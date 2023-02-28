package ru.practicum.stats.dto;

import lombok.*;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class StatsDtoOut {
    String app;
    String uri;
    Long hits;
}
