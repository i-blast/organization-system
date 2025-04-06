package com.pii.user_service.service;

import com.pii.user_service.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto findUserById(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    List<UserDto> findAllUsers();

    List<UserDto> findUserByCompany(Long companyId);

    void deleteUser(Long id);
}
