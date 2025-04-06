package com.pii.shared.util;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserDto;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
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
