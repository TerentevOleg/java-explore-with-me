package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.comment.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping("/{userId}/comments/{eventId}")
    public ResponseEntity<CommentDtoOut> add(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody CommentDtoOut commentDtoOut) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.add(userId, eventId, commentDtoOut));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<CommentDtoOut>> getAll(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getByUserId(userId));
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDtoOut> getById(@PathVariable Long userId,
                                                 @PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getByUserId(userId, commentId));
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDtoOut> update(@PathVariable Long userId,
                                                @PathVariable Long commentId,
                                                @RequestBody CommentDtoOut commentDtoOut) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.update(userId, commentId, commentDtoOut));
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long commentId) {
        commentService.delete(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}
