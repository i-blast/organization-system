package com.pii.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private CompanyShortDto company;

}
