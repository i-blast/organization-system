package com.pii.company_service.util;

import com.pii.company_service.entity.Company;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static Company createCompanyEntity() {
        return Company.builder()
                .name("Serious Organization")
                .budget(100000000L)
                .build();
    }
}
