package ru.practicum.mainservice.request.dto;

import lombok.*;

@Value
@Builder
public class RequestDtoOut {
    Long id;
    String created;
    Long event;
    Long requester;
    String status;
}
