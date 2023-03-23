package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDtoOut;
import ru.practicum.mainservice.comment.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin")
public class CommentAdminController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDtoOut>> getByAdmin(
                                                    @RequestParam(required = false) String text,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd
    ) {
        return ResponseEntity.ok(commentService.getByAdmin(text, from, size, rangeStart, rangeEnd));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDtoOut> updateByAdmin(@PathVariable Long commentId,
                                                       @RequestParam("approved") Boolean approved) {
        return ResponseEntity.ok(commentService.updateByAdmin(commentId, approved));
    }
}
