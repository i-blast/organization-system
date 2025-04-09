package com.pii.shared.exception;

/**
 * Thrown to indicate that a transactional operation has failed.
 *
 * @author ilYa
 */
public class TransactionalOperationException extends RuntimeException {

    /**
     * Thrown to indicate that a transactional operation has failed.
     *
     * @param message
     * @param cause
     */
    public TransactionalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
