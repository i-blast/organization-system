package com.pii.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Company data transfer object.
 *
 * @author ilYa
 */
@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class CompanyDto {

    /**
     * Company id.
     */
    private Long id;
    /**
     * Company name.
     */
    private String name;
    /**
     * Company budget in USD cents.
     */
    private Long budget;
    /**
     * List of company short view employee dtos.
     */
    private List<UserShortDto> employees;

}
