package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventAdminPatchDtoIn;
import ru.practicum.mainservice.event.dto.EventDtoOut;
import ru.practicum.mainservice.event.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDtoOut>> getByAdmin(@RequestParam List<Long> users,
                                                                 @RequestParam (required = false) List<String> states,
                                                                 @RequestParam List<Long> categories,
                                                                 @RequestParam (required = false) String rangeStart,
                                                                 @RequestParam (required = false) String rangeEnd,
                                                                 @RequestParam (name = "from", defaultValue = "0")
                                                                 Integer from,
                                                                 @RequestParam (name = "size", defaultValue = "10")
                                                                 Integer size) {
        return ResponseEntity.ok(eventService.getByAdmin(users, states,
                categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventDtoOut> updateByAdmin(@PathVariable Long eventId,
                                                     @RequestBody EventAdminPatchDtoIn eventAdminPatchDtoIn) {
        return ResponseEntity.ok(eventService.updateByAdmin(eventId, eventAdminPatchDtoIn));
    }
}
