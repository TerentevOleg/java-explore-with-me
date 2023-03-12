package ru.practicum.mainservice.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.mainservice.location.dto.LocationDtoOut;
import ru.practicum.mainservice.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toLocation(LocationDtoOut locationDtoOut);

    LocationDtoOut toLocationDto(Location location);
}
