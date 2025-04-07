package com.pii.user_service.mapper;

import com.pii.shared.dto.UserDto;
import com.pii.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
