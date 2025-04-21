package com.pii.shared.exception;

/**
 * Thrown when an external service returns an error or no data.
 *
 * @author ilYa
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
