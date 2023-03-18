package ru.practicum.mainservice.user.dto;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class UserDtoIn {

    @Email
    @NotBlank
    @NotNull
    String email;

    @NotBlank
    @NotNull
    String name;
}
