package ru.practicum.mainservice.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CategoryDtoOut {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
}
