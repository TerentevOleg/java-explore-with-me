package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.comment.model.CommentStatus;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class CommentDtoOut {

    Long id;

    @NotEmpty
    String text;

    String authorName;

    String created;

    CommentStatus state;
}