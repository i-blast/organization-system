package com.pii.shared.exception;

/**
 * Thrown to indicate that an unexpected server-side error has occurred.
 * Should result in HTTP 5xx response.
 *
 * @author ilYa
 */
public class UnexpectedException extends RuntimeException {

    public UnexpectedException(String message) {
        super(message);
    }
}
