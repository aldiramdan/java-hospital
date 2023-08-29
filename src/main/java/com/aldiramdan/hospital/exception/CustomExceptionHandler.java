package com.aldiramdan.hospital.exception;

import com.aldiramdan.hospital.exception.custom.*;
import com.aldiramdan.hospital.model.dto.response.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    private ResponseError responseError;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception e) {
        log.warn("Exception: {}", e.getMessage());
        responseError = new ResponseError(500, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.internalServerError().body(responseError);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidation(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}", e.getMessage());
        Map<String, Object> mapError = new HashMap<>();
        e.getFieldErrors().forEach(error -> mapError.put(error.getField(), error.getDefaultMessage()));
        responseError = new ResponseError(400, LocalDateTime.now(), "Error validation", mapError);
        return ResponseEntity.badRequest().body(responseError);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleBadRequestException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());
        responseError = new ResponseError(400, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.badRequest().body(responseError);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ResponseError> handleBadRequestException(BadRequestException e) {
        log.warn("BadRequestException: {}", e.getMessage());
        responseError = new ResponseError(400, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.badRequest().body(responseError);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFound(NotFoundException e) {
        log.warn("NotFoundException: {}", e.getMessage());
        responseError = new ResponseError(404, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.status(responseError.getCode()).body(responseError);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ResponseError> handleConflictException(ConflictException e) {
        log.warn("ConflictException: {}", e.getMessage());
        responseError = new ResponseError(409, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.status(responseError.getCode()).body(responseError);
    }

    @ExceptionHandler(value = NotProcessException.class)
    public ResponseEntity<ResponseError> handleNotProcess(NotProcessException e) {
        log.warn("NotProcessException: {}", e.getMessage());
        responseError = new ResponseError(422, LocalDateTime.now(), e.getMessage(), null);
        return ResponseEntity.status(responseError.getCode()).body(responseError);
    }
}
