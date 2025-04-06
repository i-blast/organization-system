package com.pii.shared.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        CompanyDto company
) {
}
