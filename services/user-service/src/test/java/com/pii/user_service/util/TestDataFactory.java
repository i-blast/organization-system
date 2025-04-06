package com.pii.user_service.util;

import com.pii.user_service.entity.User;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User createUserEntity() {
        return createUserEntityWithCompany(1L);
    }

    public static User createUserEntityWithCompany(Long companyId) {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .companyId(companyId)
                .build();
    }
}
