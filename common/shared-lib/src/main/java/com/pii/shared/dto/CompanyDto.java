package com.pii.shared.dto;

import java.util.List;

public record CompanyDto(
        Long id,
        String name,
        Long budget,
        List<Long> employeeIds
) {
}
