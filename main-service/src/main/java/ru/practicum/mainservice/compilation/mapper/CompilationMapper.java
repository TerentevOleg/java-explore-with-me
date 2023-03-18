package ru.practicum.mainservice.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.mainservice.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.compilation.dto.CompilationDtoIn;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.dto.EventShortDtoOut;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events", qualifiedByName = "toEventList")
    Compilation toCompilation(CompilationDtoIn compilationDtoIn);

    @Mapping(source = "events", target = "events", qualifiedByName = "toEventShortDtoList")
    CompilationDtoOut toCompilationDto(Compilation compilation);

    @Named("toEventList")
    default List<Event> toEventList(List<Long> eventIds) {
        return eventIds.stream().map(id -> new Event()).collect(Collectors.toList());
    }

    @Named("toEventShortDtoList")
    default List<EventShortDtoOut> toEventShortDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }
}
