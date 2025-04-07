package com.pii.company_service.exception;

import jakarta.persistence.EntityNotFoundException;

public class CompanyNotFoundException extends EntityNotFoundException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
