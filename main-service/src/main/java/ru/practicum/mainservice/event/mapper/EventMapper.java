package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.dto.EventDtoOut;
import ru.practicum.mainservice.event.dto.EventShortDtoOut;
import ru.practicum.mainservice.event.dto.EventDtoIn;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.location.dto.LocationDtoOut;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDtoOut toEventShortDto(Event event) {
        return EventShortDtoOut.builder()
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DTF))
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(EventDtoIn eventDtoIn) {
        Event event = new Event();
        event.setAnnotation(eventDtoIn.getAnnotation());
        event.setDescription(eventDtoIn.getDescription());
        event.setEventDate(LocalDateTime.parse(eventDtoIn.getEventDate(), DTF));
        event.setLocation(toLocation(eventDtoIn.getLocation()));
        event.setPaid(eventDtoIn.getPaid());
        event.setParticipantLimit(eventDtoIn.getParticipantLimit());
        event.setRequestModeration(eventDtoIn.getRequestModeration());
        event.setTitle(eventDtoIn.getTitle());
        return event;
    }

    public static EventDtoOut toEventFullDto(Event event) {
        return EventDtoOut.builder()
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DTF))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DTF))
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(LocalDateTime.now().format(DTF))
                .state(event.getState())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    private static CategoryDtoOut toCategoryDto(Category category) {
        return CategoryDtoOut.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private static LocationDtoOut toLocationDto(Location location) {
        return LocationDtoOut.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

    private static Location toLocation(LocationDtoOut locationDtoOut) {
        Location location = new Location();
        location.setLat(locationDtoOut.getLat());
        location.setLon(locationDtoOut.getLon());
        return location;
    }

    private static UserShortDtoOut toUserShortDto(User user) {
        return UserShortDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
