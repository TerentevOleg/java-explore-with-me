package ru.practicum.mainservice.location.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationDtoOut {
    Float lat;
    Float lon;
}
