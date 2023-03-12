package ru.practicum.mainservice.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.category.dto.CategoryDtoIn;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryDtoIn categoryDtoIn);

    CategoryDtoOut toCategoryDto(Category category);
}
