package com.pii.user_service.exception;

import com.pii.shared.exception.ExternalServiceException;
import com.pii.shared.exception.TransactionalOperationException;
import com.pii.shared.exception.UnexpectedException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(UserNotFoundException exc) {
        log.error("User not found: {}", exc.getMessage());
        return buildErrorResponse(exc, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exc) {
        log.error("Unexpected error occurred", exc);
        return buildErrorResponse(
                new UnexpectedException("Unexpected server error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exc) {
        String message = exc.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("Validation failed: {}", message);
        return buildErrorResponse(
                new UnexpectedException(message),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exc) {
        String message = exc.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s",
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .collect(Collectors.joining("; "));
        log.error("Validation failed: {}", message);
        return buildErrorResponse(
                new UnexpectedException(message),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TransactionalOperationException.class)
    public ResponseEntity<ErrorResponse> handleTransactionalOperationException(TransactionalOperationException exc) {
        log.error("Transactional operation failed", exc);
        return buildErrorResponse(exc, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException exc) {
        log.error("External service error", exc);
        return buildErrorResponse(exc, HttpStatus.SERVICE_UNAVAILABLE);
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
