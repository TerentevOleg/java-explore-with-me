package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;

@Data
@Builder
public class EventShortDtoOut {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDtoOut category;

    private Long confirmedRequests;

    private String eventDate;

    private UserShortDtoOut initiator;

    private Boolean paid;

    private Long views;
}
