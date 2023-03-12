package ru.practicum.mainservice.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ErrorDto {

    List<String> errors;

    String message;

    String reason;

    HttpStatus status;

    LocalDateTime timestamp;
}
