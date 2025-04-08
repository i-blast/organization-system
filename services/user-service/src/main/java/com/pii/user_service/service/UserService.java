package com.pii.user_service.service;

import com.pii.shared.dto.UserDto;
import com.pii.user_service.dto.CreateUserRequest;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserRequest createUserRequest);

    UserDto findUserById(Long id);

    UserDto updateUser(Long id, CreateUserRequest createUserRequest);

    List<UserDto> findAllUsers();

    List<UserDto> findUsersByCompany(Long companyId);

    void deleteUser(Long id);
}
