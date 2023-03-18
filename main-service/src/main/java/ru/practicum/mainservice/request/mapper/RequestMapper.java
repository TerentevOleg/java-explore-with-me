package ru.practicum.mainservice.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.request.dto.RequestDtoOut;
import ru.practicum.mainservice.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "requester.id", target = "requester")
    @Mapping(source = "status", target = "status")
    RequestDtoOut toRequestDtoOut(Request request);
}
