package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.request.dto.RequestPatchDtoIn;
import ru.practicum.mainservice.request.dto.RequestPatchDtoOut;
import ru.practicum.mainservice.request.dto.RequestDtoOut;

import java.util.List;

public interface RequestService {

    RequestDtoOut add(Long userId, Long eventId);

    List<RequestDtoOut> getById(Long userId);

    List<RequestDtoOut> getByEventId(Long userId, Long eventId);

    RequestPatchDtoOut update(Long userId, Long eventId, RequestPatchDtoIn requestPatchDtoIn);

    RequestDtoOut delete(Long userId, Long requestId);
}
