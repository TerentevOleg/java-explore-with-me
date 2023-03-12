package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;

@Value
@Builder
public class EventShortDtoOut {

    Long id;

    String title;

    String annotation;

    CategoryDtoOut category;

    Long confirmedRequests;

    String eventDate;

    UserShortDtoOut initiator;

    Boolean paid;

    Long views;
}
