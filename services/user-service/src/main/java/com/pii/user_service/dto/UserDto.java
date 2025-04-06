package com.pii.user_service.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        CompanyDto company
) {
}
