package com.pii.company_service.mapper;

import com.pii.company_service.entity.Company;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserDto;
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
    Company toEntity(CompanyDto companyDto);

    @Named("mapEmployeeIdsToUsers")
    default List<UserDto> mapEmployeeIdsToUsers(List<Long> employeeIds) {
        return employeeIds.stream()
                .map(id -> new UserDto(id, null, null, null, null))
                .toList();
    }

    @Named("mapUsersToEmployeeIds")
    default List<Long> mapUsersToEmployeeIds(List<UserDto> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(UserDto::id)
                .toList();
    }
}
