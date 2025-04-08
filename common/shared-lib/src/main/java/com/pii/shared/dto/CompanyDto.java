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

    private final Long id;

    private final String name;

    private final Long budget;

    private final List<UserShortDto> employees;

}
