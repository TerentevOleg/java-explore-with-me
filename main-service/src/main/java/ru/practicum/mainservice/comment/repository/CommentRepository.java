package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthorId(Long id);

    List<Comment> findCommentsByEventId(Long id);

    List<Comment> findAllByEventIdAndState(Long id, CommentStatus state);

    Comment findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByTextContainingAndCreatedBetween(String text, LocalDateTime start,
                                                           LocalDateTime end, Pageable pageable);

    List<Comment> findCommentsByEventIdIn(List<Long> eventIds);
}
