package ru.practicum.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.model.CommentStatus;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ContentDetectedException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public CommentDtoOut add(Long userId, Long eventId, CommentDtoOut commentDtoOut) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("CommentServiceImpl: user with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("CommentServiceImpl: event with id=" + eventId + " was not found."));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ContentDetectedException("CommentServiceImpl: comments can only be left for published events.");
        }
        Comment comment = commentMapper.toComment(commentDtoOut);
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentStatus.PENDING);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDtoOut getByUserId(Long userId, Long commentId) {
        Comment comment = getComment(userId, commentId);
        if (Objects.isNull(comment)) {
            throw new NotFoundException("CommentServiceImpl: comment with id=" + commentId + " by user with id=" +
                    userId + " was not found.");
        }
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDtoOut> getByUserId(Long userId) {
        List<Comment> list = commentRepository.findAllByAuthorId(userId);
        return list.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDtoOut> getByEventId(Long id) {
        List<Comment> list = commentRepository.findAllByEventIdAndState(id, CommentStatus.PUBLISHED);
        return list.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDtoOut> getByAdmin(String text, Integer from, Integer size, String rangeStart, String rangeEnd) {
        LocalDateTime start;
        LocalDateTime end;
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            start = LocalDateTime.now().withNano(0).minusDays(1L);
            end = LocalDateTime.now().withNano(0).plusMinutes(1L);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        List<Comment> list = commentRepository.findAllByTextContainingAndCreatedBetween(text, start, end,
                PageRequest.of(from, size));
        return list.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDtoOut updateByAdmin(Long commentId, Boolean approved) {
        Comment comment = getComment(commentId);
        if (approved) {
            comment.setState(CommentStatus.PUBLISHED);
        } else comment.setState(CommentStatus.CANCELED);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    @Override
    public CommentDtoOut update(Long userId, Long commentId, CommentDtoOut commentDtoOut) {
        Comment comment = getComment(userId, commentId);
        comment.setText(commentDtoOut.getText());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        if (comment.getAuthor().getId().equals(userId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new ContentDetectedException("CommentServiceImpl: only the author of a comment can delete it.");
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("CommentServiceImpl: comment with id=" + commentId + " was not found."));
    }

    private Comment getComment(Long userId, Long commentId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId);
    }
}