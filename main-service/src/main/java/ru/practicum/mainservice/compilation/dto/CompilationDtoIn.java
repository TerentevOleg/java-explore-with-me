package ru.practicum.mainservice.compilation.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class CompilationDtoIn {

    List<Long> events;

    Boolean pinned;

    @NotNull
    String title;
}
