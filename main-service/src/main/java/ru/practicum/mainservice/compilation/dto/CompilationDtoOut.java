package ru.practicum.mainservice.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.event.dto.EventShortDtoOut;

import java.util.List;

@Value
@Builder
public class CompilationDtoOut {
    List<EventShortDtoOut> events;
    Long id;
    Boolean pinned;
    String title;
}
