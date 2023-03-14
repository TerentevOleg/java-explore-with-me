package ru.practicum.mainservice.event.dto;

import lombok.Value;
import ru.practicum.mainservice.event.model.Event;

@Value
public class EventViewsDtoOut {

    Event event;
    Long views;
}
