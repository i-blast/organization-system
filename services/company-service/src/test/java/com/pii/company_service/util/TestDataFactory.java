package com.pii.company_service.util;

import com.pii.company_service.entity.Company;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static Company createCompanyEntity() {
        return Company.builder()
                .name("Serious Organization")
                .budget(1_000_000.0)
                .employees(List.of(1L, 2L, 3L))
                .build();
    }
}
