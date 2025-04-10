package com.pii.user_service.service;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CompanyShortDto;
import com.pii.shared.dto.UserDto;
import com.pii.shared.exception.ExternalServiceException;
import com.pii.shared.exception.TransactionalOperationException;
import com.pii.user_service.client.CompanyClient;
import com.pii.user_service.dto.CreateUserRequest;
import com.pii.user_service.entity.User;
import com.pii.user_service.exception.UserNotFoundException;
import com.pii.user_service.mapper.UserMapper;
import com.pii.user_service.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CompanyClient companyClient;

    @Transactional
    @Override
    public UserDto createUser(CreateUserRequest createUserRequest) {

        log.info("Check company exists ID: {}", createUserRequest.companyId());
        ResponseEntity<CompanyDto> companyResponse = companyClient.getCompanyById(createUserRequest.companyId());
        if (!companyResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(companyResponse.getBody())) {
            log.error("Failed to receive company data id={}", createUserRequest.companyId());
            throw new ExternalServiceException("Failed to receive company data id=" + createUserRequest.companyId());
        }

        try {
            var savedUser = userRepository.save(userMapper.toEntity(createUserRequest));
            log.info("User created successfully id={}", savedUser.getId());

            assignUserToCompany(savedUser, createUserRequest.companyId());
            log.info("User id={} assigned to company id={} successfully", savedUser.getId(), createUserRequest.companyId());

            var userDto = userMapper.toDto(savedUser);
            userDto.setCompany(new CompanyShortDto(
                    companyResponse.getBody().getId(),
                    companyResponse.getBody().getName(),
                    companyResponse.getBody().getBudget()
            ));

            return userDto;

        } catch (Exception exc) {
            log.error(
                    "Failed to create user={} and assign it to company id={}",
                    createUserRequest,
                    createUserRequest.companyId(),
                    exc
            );
            throw new TransactionalOperationException("Failed to create user and assign it to company", exc);
        }
    }

    private void assignUserToCompany(User user, Long companyId) {
        var response = companyClient.assignUserToCompany(user.getId(), companyId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException("Failed to assign user to company");
        }
    }

    private void unassignUserFromCompany(User user, Long companyId) {
        var response = companyClient.unassignUserFromCompany(user.getId(), companyId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException("Failed to unassign user from company");
        }
    }

    @Override
    public UserDto findUserById(Long id) {

        var savedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        var companyResponse = companyClient.getCompanyById(savedUser.getCompanyId());
        if (!companyResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(companyResponse.getBody())) {
            throw new ExternalServiceException("Failed to receive company data");
        }

        var userDto = userMapper.toDto(savedUser);
        userDto.setCompany(new CompanyShortDto(
                companyResponse.getBody().getId(),
                companyResponse.getBody().getName(),
                companyResponse.getBody().getBudget()
        ));
        return userDto;
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, CreateUserRequest createUserRequest) {

        var savedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        var companyResponse = companyClient.getCompanyById(createUserRequest.companyId());
        if (!companyResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(companyResponse.getBody())) {
            throw new ExternalServiceException("Company not found userId=" + createUserRequest.companyId());
        }

        savedUser.setFirstName(createUserRequest.firstName());
        savedUser.setLastName(createUserRequest.lastName());
        savedUser.setPhoneNumber(createUserRequest.phoneNumber());
        savedUser.setCompanyId(createUserRequest.companyId());

        try {
            var updatedUser = userRepository.save(savedUser);
            log.info("User updated successfully userId={}", updatedUser.getId());

            assignUserToCompany(updatedUser, createUserRequest.companyId());
            log.info("User userId={} assigned to company userId={} successfully", updatedUser.getId(), createUserRequest.companyId());

            var userDto = userMapper.toDto(updatedUser);
            userDto.setCompany(new CompanyShortDto(
                    companyResponse.getBody().getId(),
                    companyResponse.getBody().getName(),
                    companyResponse.getBody().getBudget()
            ));

            return userDto;

        } catch (Exception exc) {
            log.error("Failed to update user id={} and assign it to company id={}", userId, createUserRequest.companyId(), exc);
            throw new TransactionalOperationException("Failed to update user and assign it to company", exc);
        }
    }

    @Override
    public List<UserDto> findAllUsers() {

        var allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return List.of();
        }

        var userIds = allUsers.stream().map(User::getId).toList();
        var companiesByUsersResponse = companyClient.getCompaniesByUsers(userIds);
        if (!companiesByUsersResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(companiesByUsersResponse.getBody())) {
            throw new ExternalServiceException("Companies data receiving failed usersId=" + userIds);
        }

        var companiesByUserId = companiesByUsersResponse.getBody();
        return allUsers.stream()
                .map(user -> mapToUserDto(user, companiesByUserId.get(user.getId())))
                .toList();
    }

    private UserDto mapToUserDto(User user, CompanyDto companyDto) {
        var userDto = userMapper.toDto(user);
        if (companyDto != null) {
            userDto.setCompany(new CompanyShortDto(
                    companyDto.getId(),
                    companyDto.getName(),
                    companyDto.getBudget()
            ));
        }
        return userDto;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {

        var savedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        var companyId = savedUser.getCompanyId();

        try {
            userRepository.deleteById(userId);
            log.info("User deleted successfully userId={}", userId);

            unassignUserFromCompany(savedUser, companyId);
            log.info("User userId={} unassigned from company userId={} successfully", userId, companyId);
        } catch (Exception exc) {
            log.error("Failed to delete user id={} and unassign it from company id={}", userId, companyId, exc);
            throw new TransactionalOperationException("Failed to update user and unassign it from company", exc);
        }
    }

    @Override
    public List<UserDto> getUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userRepository.findAllById(userIds).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findUsersByCompany(Long companyId) {

        var users = userRepository.findByCompanyId(companyId);
        if (users.isEmpty()) {
            return List.of();
        }

        var companyResponse = companyClient.getCompanyById(companyId);
        if (!companyResponse.getStatusCode().is2xxSuccessful() || companyResponse.getBody() == null) {
            throw new ExternalServiceException("Company not found with id: " + companyId);
        }

        var companyShortDto = new CompanyShortDto(
                companyResponse.getBody().getId(),
                companyResponse.getBody().getName(),
                companyResponse.getBody().getBudget()
        );
        return users.stream()
                .map(user -> {
                    UserDto userDto = userMapper.toDto(user);
                    userDto.setCompany(companyShortDto);
                    return userDto;
                })
                .toList();
    }
}
