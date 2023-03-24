package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.dto.CommentDtoOut;

import java.util.List;

public interface CommentService {

    CommentDtoOut add(Long userId, Long eventId, CommentDtoOut commentDtoOut);

    List<CommentDtoOut> getByUserId(Long userId);

    List<CommentDtoOut> getByEventId(Long id);

    CommentDtoOut getByUserId(Long userId, Long commentId);

    List<CommentDtoOut> getByAdmin(String text, Integer from, Integer size, String rangeStart, String rangeEnd);

    CommentDtoOut updateByAdmin(Long commentId, Boolean approved);

    CommentDtoOut update(Long userId, Long commentId, CommentDtoOut commentDtoOut);

    void delete(Long userId, Long commentId);
}
