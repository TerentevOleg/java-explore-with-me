package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.location.dto.LocationDtoOut;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class EventDtoIn {

    @NotNull
    String annotation;

    Long category;

    String title;

    @NotNull
    String description;

    String eventDate;

    LocationDtoOut location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;
}

