package com.pii.user_service.util;

import com.pii.user_service.model.User;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User createTestUser() {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .companyId(1L)
                .build();
    }

    public static User createTestUserWithCompany(Long companyId) {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .companyId(companyId)
                .build();
    }
}
