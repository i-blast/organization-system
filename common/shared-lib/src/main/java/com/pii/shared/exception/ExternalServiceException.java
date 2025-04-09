package com.pii.shared.exception;

/**
 * Thrown when an external service returns an error or no data.
 *
 * @author ilYa
 */
public class ExternalServiceException extends RuntimeException {

    /**
     * Thrown when an external service returns an error or no data.
     *
     * @param message message
     */
    public ExternalServiceException(String message) {
        super(message);
    }

    /**
     * Thrown when an external service returns an error or no data.
     */
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
