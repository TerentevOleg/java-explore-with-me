package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;

import java.util.List;

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

    private List<CommentDtoOut> comments;
}
