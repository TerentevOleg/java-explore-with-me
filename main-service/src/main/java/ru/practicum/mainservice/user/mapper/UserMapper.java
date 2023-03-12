package ru.practicum.mainservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.user.dto.UserDtoIn;
import ru.practicum.mainservice.user.dto.UserDtoOut;
import ru.practicum.mainservice.user.dto.UserShortDtoOut;
import ru.practicum.mainservice.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserDtoIn userDtoIn);

    UserDtoOut toUserDto(User user);

    UserShortDtoOut toUserShortDto(User user);
}
