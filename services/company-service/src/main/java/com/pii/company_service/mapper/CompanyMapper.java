package com.pii.company_service.mapper;

import com.pii.company_service.dto.CreateCompanyRequest;
import com.pii.company_service.entity.Company;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserShortDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "employees", source = "company.id", qualifiedByName = "mapCompanyIdToUsers")
    CompanyDto toDto(Company company, @Context ToEmployeesMapping mapping);

    Company toEntity(CreateCompanyRequest request);

    @Named("mapCompanyIdToUsers")
    static List<UserShortDto> mapCompanyIdToUsers(Long companyId, @Context ToEmployeesMapping mapping) {
        return mapping.mapCompanyToUsers(companyId);
    }
}
