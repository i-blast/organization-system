package com.pii.user_service.service;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CreateUserRequest;
import com.pii.shared.dto.UserDto;
import com.pii.shared.dto.UserShortDto;
import com.pii.shared.exception.ExternalServiceException;
import com.pii.user_service.client.CompanyClient;
import com.pii.user_service.entity.User;
import com.pii.user_service.exception.UserNotFoundException;
import com.pii.user_service.mapper.UserMapper;
import com.pii.user_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.pii.user_service.util.TestDataFactory.createUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
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
        User user = createUserEntity();
        CompanyDto companyDto = new CompanyDto(1L, "TestCompany", 1000L, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyClient.getCompanyById(1L)).thenReturn(ResponseEntity.ok(companyDto));

        UserDto result = userService.findUserById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getCompany().name()).isEqualTo("TestCompany");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findUserById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldThrowWhenCompanyNotFoundOnCreate() {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "123456", 1L);
        when(companyClient.getCompanyById(1L)).thenReturn(ResponseEntity.notFound().build());
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Failed to receive company data: companyId=1");
    }

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "123456", 1L);
        CompanyDto companyDto = new CompanyDto(1L, "TestCompany", 1000L, null);
        User userEntity = userMapper.toEntity(request);
        when(companyClient.getCompanyById(1L)).thenReturn(ResponseEntity.ok(companyDto));
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(companyClient.assignUserToCompany(any(), any())).thenReturn(ResponseEntity.ok().build());

        UserDto createdUserDto = userService.createUser(request);

        assertThat(createdUserDto).isNotNull();
        assertThat(createdUserDto.getFirstName()).isEqualTo("John");
        assertThat(createdUserDto.getCompany()).isNotNull();
        assertThat(createdUserDto.getCompany().id()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateUser() {
        User user = createUserEntity();
        CreateUserRequest request = new CreateUserRequest("Updated", "Doe", "654321", 2L);
        CompanyDto companyDto = new CompanyDto(
                1L,
                "NewCompany",
                1000000000L,
                List.of(new UserShortDto(1L, "John", "Doe", "+1234567890"))
        );
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(companyClient.getCompanyById(2L)).thenReturn(ResponseEntity.ok(companyDto));
        when(companyClient.assignUserToCompany(any(), any())).thenReturn(ResponseEntity.ok().build());

        UserDto updatedUserDto = userService.updateUser(1L, request);

        assertThat(updatedUserDto.getFirstName()).isEqualTo("Updated");
        assertThat(updatedUserDto.getCompany().name()).isEqualTo("NewCompany");
    }

    @Test
    void shouldThrowWhenCompanyNotFoundOnUpdate() {
        User user = createUserEntity();
        CreateUserRequest request = new CreateUserRequest("Updated", "Doe", "654321", 2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyClient.getCompanyById(2L)).thenReturn(ResponseEntity.notFound().build());
        assertThatThrownBy(() -> userService.updateUser(1L, request))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Failed to receive company data: companyId=2");
    }

    @Test
    void shouldFindAllUsers() {
        User user = new User(1L, "John", "Doe", "123456", 1L);
        CompanyDto companyDto = new CompanyDto(1L, "TestCompany", 1000L, null);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(companyClient.getCompaniesByUsers(List.of(1L))).thenReturn(ResponseEntity.ok(Map.of(1L, companyDto)));

        List<UserDto> result = userService.findAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompany().name()).isEqualTo("TestCompany");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = createUserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyClient.unassignUserFromCompany(1L, 1L)).thenReturn(ResponseEntity.ok().build());
        userService.deleteUser(1L);
    }

    @Test
    void shouldThrowWhenUserNotFoundOnDelete() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldReturnUsersByIds() {
        User user1 = createUserEntity();
        user1.setId(1L);
        User user2 = createUserEntity();
        user2.setId(2L);
        when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.getUsersByIds(List.of(1L, 2L));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }
}
