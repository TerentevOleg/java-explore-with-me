package ru.practicum.mainservice.compilation.service;

import ru.practicum.mainservice.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.compilation.dto.CompilationDtoIn;
import ru.practicum.mainservice.compilation.dto.CompilationPatchDtoIn;

import java.util.List;

public interface CompilationService {

    CompilationDtoOut add(CompilationDtoIn compilationDtoIn);

    CompilationDtoOut getById(Long id);

    List<CompilationDtoOut> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDtoOut update(Long id, CompilationPatchDtoIn compilationPatchDtoIn);

    void delete(Long id);
}

