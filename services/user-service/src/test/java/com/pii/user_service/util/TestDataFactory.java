package com.pii.user_service.util;

import com.pii.user_service.dto.CompanyDto;
import com.pii.user_service.dto.UserDto;
import com.pii.user_service.entity.User;

import java.util.List;

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

    public static CompanyDto createCompanyDto() {
        return new CompanyDto(1L, "Test Company", 100_000L, List.of(1L));
    }

    public static UserDto createUserDtoWithCompany() {
        return createUserWithName("John");
    }

    public static UserDto createUserWithName(String firstName) {
        return new UserDto(
                1L,
                firstName,
                "Doe",
                "+1234567890",
                createCompanyDto()
        );
    }
}
