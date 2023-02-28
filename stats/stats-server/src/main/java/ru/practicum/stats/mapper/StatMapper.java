package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.dto.StatsDtoIn;
import ru.practicum.stats.model.AppEntity;
import ru.practicum.stats.model.StatEntity;

@Mapper(componentModel = "spring")
public interface StatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "app", source = "appEntity")
    StatEntity fromDto(StatsDtoIn statsDtoIn, AppEntity appEntity);
}
