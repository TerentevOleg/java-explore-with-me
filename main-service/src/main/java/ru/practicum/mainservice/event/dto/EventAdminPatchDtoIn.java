package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.event.model.StateAction;
import ru.practicum.mainservice.location.dto.LocationDtoOut;

@Value
@Builder
public class EventAdminPatchDtoIn {

    String title;

    String annotation;

    Long category;

    String description;

    String eventDate;

    LocationDtoOut location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    StateAction stateAction;
}
