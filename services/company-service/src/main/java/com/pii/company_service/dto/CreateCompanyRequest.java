package com.pii.company_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCompanyRequest(

        @NotBlank(message = "Company name must not be blank")
        @Size(max = 255, message = "Company name must be at most 255 characters")
        String name,

        @NotNull(message = "Budget must not be null")
        @PositiveOrZero(message = "Budget must be zero or positive")
        Long budget, // Budget in USD cents.

        @NotNull(message = "Employees list must not be null")
        List<Long> employees
) {
}
