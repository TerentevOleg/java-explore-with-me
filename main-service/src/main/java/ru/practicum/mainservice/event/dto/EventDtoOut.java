package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.dto.LocationDtoOut;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;

@Value
@Builder
public class EventDtoOut {

    Long id;

    String title;

    String annotation;

    CategoryDtoOut category;

    Long confirmedRequests;

    String createdOn;

    String description;

    String eventDate;

    UserShortDtoOut initiator;

    LocationDtoOut location;

    Boolean paid;

    Long participantLimit;

    String publishedOn;

    State state;

    Boolean requestModeration;

    Long views;
}
