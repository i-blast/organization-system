package com.pii.user_service.service;

import com.pii.user_service.client.CompanyClient;
import com.pii.user_service.mapper.UserMapper;
import com.pii.user_service.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.pii.shared.util.TestDataFactory.*;
import static com.pii.user_service.util.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyClient companyClient;

    private UserMapper userMapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
        userService = new UserServiceImpl(userMapper, userRepository, companyClient);
    }

    @Test
    void shouldFindUserById() {
        var user = createUserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        var result = userService.findUserById(1L);
        assertThat(result.firstName()).isEqualTo("John");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findUserById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found.");
    }

    @Test
    void shouldCreateUser() {
        var userDto = createUserDtoWithCompany();
        var userEntity = userMapper.toEntity(userDto);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        var result = userService.createUser(userDto);

        assertThat(result.firstName()).isEqualTo(userDto.firstName());
        assertThat(result.lastName()).isEqualTo(userDto.lastName());
        assertThat(result.phoneNumber()).isEqualTo(userDto.phoneNumber());
    }

    @Test
    void shouldUpdateUser() {
        var user = createUserEntity();
        var updatedDto = createUserWithName("UpdatedName");
        var companyDto = createCompanyDto();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = userService.updateUser(user.getId(), updatedDto);

        assertThat(result.firstName()).isEqualTo("UpdatedName");
    }

    @Test
    void shouldFindAllUsers() {
        var user = createUserEntity();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(companyClient.getCompanyById(user.getCompanyId()))
                .thenReturn(userMapper.toDto(user).company());

        var result = userService.findAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo(user.getFirstName());
    }
}
