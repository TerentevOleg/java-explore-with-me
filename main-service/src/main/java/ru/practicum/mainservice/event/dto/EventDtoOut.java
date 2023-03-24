package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.dto.LocationDtoOut;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;

import java.util.List;

@Data
@Builder
public class EventDtoOut {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDtoOut category;

    private Long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserShortDtoOut initiator;

    private LocationDtoOut location;

    private Boolean paid;

    private Long participantLimit;

    private String publishedOn;

    private State state;

    private Boolean requestModeration;

    private Long views;

    private List<CommentDtoOut> comments;
}
