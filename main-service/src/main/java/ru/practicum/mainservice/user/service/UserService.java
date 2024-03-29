package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.dto.UserDtoIn;
import ru.practicum.mainservice.user.dto.UserDtoOut;

import java.util.List;

public interface UserService {
    UserDtoOut add(UserDtoIn userDtoIn);

    List<UserDtoOut> getAll(List<Long> ids, Integer from, Integer size);

    void delete(Long id);
}
