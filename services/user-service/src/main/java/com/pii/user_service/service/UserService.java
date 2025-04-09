package com.pii.user_service.service;

import com.pii.shared.dto.UserDto;
import com.pii.user_service.dto.CreateUserRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserRequest createUserRequest);

    UserDto findUserById(Long id);

    UserDto updateUser(Long id, CreateUserRequest createUserRequest);

    List<UserDto> findAllUsers();

    List<UserDto> findUsersByCompany(Long companyId);

    void deleteUser(Long id);

    List<UserDto> getUsersByIds(@NotNull(message = "Users list must not be null") List<Long> userIds);
}
