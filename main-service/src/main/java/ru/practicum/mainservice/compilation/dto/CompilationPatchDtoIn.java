package ru.practicum.mainservice.compilation.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CompilationPatchDtoIn {

    @NonNull
    List<Long> events;
    Boolean pinned;
    String title;
}
