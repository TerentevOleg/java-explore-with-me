package ru.practicum.mainservice.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class UserDtoOut {

    Long id;

    @NotBlank
    String name;

    @NotBlank
    String email;
}
