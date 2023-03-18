package ru.practicum.mainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFound(NotFoundException e) {
        log.warn("Object not found", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.NOT_FOUND)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequest(AlreadyExistsException e) {
        log.warn("Object already exists", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleViolationException(DataIntegrityViolationException e) {
        log.warn("Db constraint violation", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleCONFLICT(ContentDetectedException e) {
        log.warn("Content not found", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleInternalServerError(HttpServerErrorException.InternalServerError e) {
        log.warn("Internal Server Error", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        log.warn("Object validation error", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .message(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .reason(e.getObjectName() + "\n" + e.getParameter())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleThrowableExceptions(Throwable e) {
        log.warn("Unexpected exception", e);
        return ErrorDto.builder()
                .errors(List.of(e.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .reason("Throwable exception")
                .timestamp(LocalDateTime.now())
                .build();
    }
}