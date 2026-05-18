package com.DebboCollect.DebboCollect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionResponse {

    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<?> handleCustomException(
            CustomResponseException ex
    ) {

        Map<String, Object> errors =
                new HashMap<>();

        errors.put("message", ex.getMessage());

        errors.put("status", ex.getStatus());

        errors.put(
                "timestamp",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errors,
                HttpStatus.valueOf(ex.getStatus())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
            Exception ex
    ) {

        Map<String, Object> errors =
                new HashMap<>();

        errors.put("message", ex.getMessage());

        errors.put("status", 500);

        errors.put(
                "timestamp",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errors,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}