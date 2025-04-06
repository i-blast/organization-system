package com.pii.user_service.mapper;

import com.pii.shared.dto.UserDto;
import com.pii.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "company", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "companyId", source = "company.id")
    User toEntity(UserDto userDto);
}
