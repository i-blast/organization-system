package com.pii.shared.util;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserDto;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static CompanyDto createCompanyDto() {
        return new CompanyDto(1L, "Test Company", 1000000000L, List.of(
                new UserDto(1L, "Test1", "Doe", "+1234567890", null),
                new UserDto(1L, "Test2", "Doe", "+1234567890", null)
        ));
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
