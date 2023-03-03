package ru.practicum.stats.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@RequiredArgsConstructor
@Jacksonized
public class StatsDtoOut {
    String app;
    String uri;
    Long hits;
}
