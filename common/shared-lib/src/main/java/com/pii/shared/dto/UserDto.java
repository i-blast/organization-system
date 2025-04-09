package com.pii.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User data transfer object.
 *
 * @author ilYa
 */
@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class UserDto {

    /**
     * User id.
     */
    private Long id;

    /**
     * User first name.
     */
    private String firstName;

    /**
     * User last name.
     */
    private String lastName;

    /**
     * User phone number.
     */
    private String phoneNumber;

    /**
     * Short view company dto.
     */
    private CompanyShortDto company;

}
