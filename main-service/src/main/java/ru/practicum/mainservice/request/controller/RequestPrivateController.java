package ru.practicum.mainservice.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.RequestDtoOut;
import ru.practicum.mainservice.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class RequestPrivateController {

    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDtoOut> add(@PathVariable Long userId,
                                             @RequestParam Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.add(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestDtoOut>> getRequestIngoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getById(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDtoOut> delete(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.delete(userId, requestId));
    }
}
