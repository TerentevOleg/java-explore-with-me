package ru.practicum.mainservice.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RequestPatchDtoIn {
    List<Long> requestIds;
    String status;
}
