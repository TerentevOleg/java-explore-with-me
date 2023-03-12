package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.dto.*;

import java.util.List;

public interface EventService {

    EventDtoOut add(Long userId, EventDtoIn eventDto);

    EventDtoOut getById(Long id);

    EventDtoOut getById(Long userId, Long eventId);

    List<EventShortDtoOut> getByUserId(Long userId, Integer from, Integer size);

    List<EventDtoOut> getByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                 String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShortDtoOut> getEventsByFilters(String text, List<Long> categories, Boolean paid,
                                              String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                              String sort, Integer from, Integer size);

    EventDtoOut updateByUserId(Long userId, Long eventId, EventUserPatchDtoIn eventUserRequest);

    EventDtoOut updateByAdmin(Long eventId, EventAdminPatchDtoIn eventAdminRequest);
}
