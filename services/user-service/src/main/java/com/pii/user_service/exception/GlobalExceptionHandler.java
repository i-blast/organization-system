package com.pii.user_service.exception;

import com.pii.shared.exception.UnexpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(UserNotFoundException exc) {
        return buildErrorResponse(
                exc,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exc) {
        return buildErrorResponse(
                new UnexpectedException("Unexpected server error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exc, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                exc.getMessage()
        );
        return new ResponseEntity<>(error, status);
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String message
    ) {
    }
}
