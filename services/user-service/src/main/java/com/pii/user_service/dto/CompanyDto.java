package com.pii.user_service.dto;

import java.util.List;

public record CompanyDto(
        Long id,
        String name,
        Long budget,
        List<Long> employeeIds
) {
}
