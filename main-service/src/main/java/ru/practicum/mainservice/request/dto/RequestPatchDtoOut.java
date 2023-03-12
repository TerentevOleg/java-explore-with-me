package ru.practicum.mainservice.request.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RequestPatchDtoOut {
    List<RequestDtoOut> confirmedRequests;
    List<RequestDtoOut> rejectedRequests;
}
