package com.pii.user_service.mapper;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CompanyShortDto;
import com.pii.shared.dto.CreateUserRequest;
import com.pii.shared.dto.UserDto;
import com.pii.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(CreateUserRequest dto);

    CompanyShortDto toCompanyShortDto(CompanyDto dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "company", expression = "java(companyDto != null ? toCompanyShortDto(companyDto) : null)")
    UserDto toDto(User user, CompanyDto companyDto);

}
