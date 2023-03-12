package ru.practicum.mainservice.category.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDtoIn {

    @NotNull
    String name;
}
