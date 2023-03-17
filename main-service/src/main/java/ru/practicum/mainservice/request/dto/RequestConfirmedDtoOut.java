package ru.practicum.mainservice.request.dto;

import lombok.Value;

@Value
public class RequestConfirmedDtoOut {
    Long eventId;
    Long count;
}