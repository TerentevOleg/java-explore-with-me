package ru.practicum.mainservice.category.service;

import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.dto.CategoryDtoIn;

import java.util.List;

public interface CategoryService {

    CategoryDtoOut add(CategoryDtoIn categoryDtoIn);

    CategoryDtoOut getById(Long id);

    List<CategoryDtoOut> getAll(Integer from, Integer size);

    CategoryDtoOut update(Long id, CategoryDtoOut categoryDtoOut);

    void delete(Long id);
}
