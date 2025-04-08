package com.pii.shared.util;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CompanyShortDto;
import com.pii.shared.dto.UserDto;
import com.pii.shared.dto.UserShortDto;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static CompanyDto createCompanyDto() {
        return new CompanyDto(
                1L,
                "Test Company",
                1000000000L,
                List.of(new UserShortDto(1L, "John", "Doe", "+1234567890"))
        );
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
                new CompanyShortDto(1L, "Test Company", 1000000000L)
        );
    }
}
