package ru.practicum.mainservice.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserShortDtoOut {
    Long id;
    String name;
}
