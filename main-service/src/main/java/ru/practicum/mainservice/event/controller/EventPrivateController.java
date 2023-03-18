package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventDtoIn;
import ru.practicum.mainservice.event.dto.EventDtoOut;
import ru.practicum.mainservice.event.dto.EventShortDtoOut;
import ru.practicum.mainservice.event.dto.EventUserPatchDtoIn;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.request.dto.RequestDtoOut;
import ru.practicum.mainservice.request.dto.RequestPatchDtoIn;
import ru.practicum.mainservice.request.dto.RequestPatchDtoOut;
import ru.practicum.mainservice.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventDtoOut> add(@PathVariable Long userId,
                                           @RequestBody @Valid EventDtoIn eventDtoIn) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.add(userId, eventDtoIn));
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDtoOut>> getByUserId(@PathVariable Long userId,
                                                              @RequestParam(name = "from", defaultValue = "0")
                                                              Integer from,
                                                              @RequestParam (name = "size", defaultValue = "10")
                                                              Integer size) {
        return ResponseEntity.ok(eventService.getByUserId(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventDtoOut> getByUserIdAndEventId(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getById(userId, eventId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDtoOut>> getRequestsByUserId(@PathVariable Long userId,
                                                                   @PathVariable Long eventId) {

        return ResponseEntity.ok(requestService.getByEventId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestPatchDtoOut> updateRequestsStatus(@PathVariable Long userId,
                                                                   @PathVariable Long eventId,
                                                                   @RequestBody RequestPatchDtoIn requestPatchDtoIn) {
        return ResponseEntity.ok(requestService.update(userId, eventId, requestPatchDtoIn));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventDtoOut> updateEventByUser(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody EventUserPatchDtoIn eventUserPatchDtoIn) {
        return ResponseEntity.ok(eventService.updateByUserId(userId, eventId, eventUserPatchDtoIn));
    }
}
