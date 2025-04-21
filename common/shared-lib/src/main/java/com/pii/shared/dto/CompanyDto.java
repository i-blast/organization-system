package com.pii.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class CompanyDto {

    private Long id;

    private String name;
    /**
     * Company budget in USD cents.
     */
    private Long budget;

    private List<UserShortDto> employees;

}
