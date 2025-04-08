package com.pii.company_service.mapper;

import com.pii.company_service.dto.CreateCompanyRequest;
import com.pii.company_service.entity.Company;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserShortDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "employees", source = "employees", qualifiedByName = "mapEmployeeIdsToUsers")
    CompanyDto toDto(Company company);

    @Mapping(target = "employees", source = "employees", qualifiedByName = "mapUsersToEmployeeIds")
    Company toEntity(CreateCompanyRequest companyDto);

    @Named("mapEmployeeIdsToUsers")
    static List<UserShortDto> mapEmployeeIdsToUsers(List<Long> employeeIds) {
        // TODO
        // Здесь должен быть вызов клиента (например, userService или userClient)
        // Сейчас можно оставить заглушку:
        return employeeIds.stream()
                .map(id -> new UserShortDto(id, "FirstName" + id, "LastName" + id, "000-000-000" + id))
                .toList();
    }

    @Named("mapUsersToEmployeeIds")
    static List<Long> mapUsersToEmployeeIds(List<Long> users) {
        return users;
    }
}
