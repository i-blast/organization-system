package com.pii.shared.exception;

public class TransactionalOperationException extends RuntimeException {

    public TransactionalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
