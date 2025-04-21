package com.pii.shared.exception;

/**
 * Thrown to indicate that a transactional operation has failed.
 *
 * @author ilYa
 */
public class TransactionalOperationException extends RuntimeException {

    public TransactionalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
