package com.pii.user_service.service;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CompanyShortDto;
import com.pii.shared.dto.CreateUserRequest;
import com.pii.shared.dto.UserDto;
import com.pii.shared.exception.ExternalServiceException;
import com.pii.shared.exception.TransactionalOperationException;
import com.pii.user_service.client.CompanyClient;
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
import java.util.Map;
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

        Long companyId = createUserRequest.companyId();
        CompanyDto companyDto = extractCompanyData(companyClient.getCompanyById(companyId), companyId);

        try {
            User savedUser = userRepository.save(userMapper.toEntity(createUserRequest));
            assignUserToCompany(savedUser, companyId);

            UserDto userDto = userMapper.toDto(savedUser);
            userDto.setCompany(new CompanyShortDto(
                    companyDto.getId(),
                    companyDto.getName(),
                    companyDto.getBudget()
            ));
            log.info(
                    "User created successfully id={}. User assigned to company id={} successfully",
                    savedUser.getId(),
                    companyId
            );
            return userDto;

        } catch (Exception exc) {
            log.error(
                    "Failed to create user={} and assign it to company id={}",
                    createUserRequest,
                    companyId,
                    exc
            );
            throw new TransactionalOperationException("Failed to create user and assign it to company", exc);
        }
    }

    private CompanyDto extractCompanyData(ResponseEntity<CompanyDto> response, Long companyId) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ExternalServiceException("Failed to receive company data: companyId=" + companyId);
        }
        return response.getBody();
    }

    private void assignUserToCompany(User user, Long companyId) {
        ResponseEntity<Void> assignResponse = companyClient.assignUserToCompany(user.getId(), companyId);
        if (!assignResponse.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException("Failed to assign user to company");
        }
    }

    private void unassignUserFromCompany(User user, Long companyId) {
        ResponseEntity<Void> unassignResponse = companyClient.unassignUserFromCompany(user.getId(), companyId);
        if (!unassignResponse.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException("Failed to unassign user from company");
        }
    }

    @Override
    public UserDto findUserById(Long id) {
        User savedUser = findUserOrThrow(id);
        Long companyId = savedUser.getCompanyId();
        CompanyDto companyDto = extractCompanyData(companyClient.getCompanyById(companyId), companyId);

        UserDto userDto = userMapper.toDto(savedUser);
        userDto.setCompany(new CompanyShortDto(
                companyDto.getId(),
                companyDto.getName(),
                companyDto.getBudget()
        ));
        return userDto;
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found id=" + userId));
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, CreateUserRequest createUserRequest) {

        User savedUser = findUserOrThrow(userId);
        Long companyId = createUserRequest.companyId();
        CompanyDto companyDto = extractCompanyData(companyClient.getCompanyById(companyId), companyId);

        savedUser.setFirstName(createUserRequest.firstName());
        savedUser.setLastName(createUserRequest.lastName());
        savedUser.setPhoneNumber(createUserRequest.phoneNumber());
        savedUser.setCompanyId(createUserRequest.companyId());

        try {
            User updatedUser = userRepository.save(savedUser);
            assignUserToCompany(updatedUser, createUserRequest.companyId());

            UserDto userDto = userMapper.toDto(updatedUser);
            userDto.setCompany(new CompanyShortDto(
                    companyDto.getId(),
                    companyDto.getName(),
                    companyDto.getBudget()
            ));
            log.info(
                    "User updated successfully userId={}. User assigned to company companyId={} successfully",
                    updatedUser.getId(),
                    createUserRequest.companyId()
            );
            return userDto;

        } catch (Exception exc) {
            log.error("Failed to update user id={} and assign it to company id={}", userId, createUserRequest.companyId(), exc);
            throw new TransactionalOperationException("Failed to update user and assign it to company", exc);
        }
    }

    @Override
    public List<UserDto> findAllUsers() {

        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = allUsers.stream().map(User::getId).toList();
        ResponseEntity<Map<Long, CompanyDto>> companiesByUsersResponse = companyClient.getCompaniesByUsers(userIds);
        if (!companiesByUsersResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(companiesByUsersResponse.getBody())) {
            throw new ExternalServiceException("Companies data receiving failed usersId=" + userIds);
        }

        Map<Long, CompanyDto> companiesByUserId = companiesByUsersResponse.getBody();
        return allUsers.stream()
                .map(user -> mapToUserDto(user, companiesByUserId.get(user.getId())))
                .toList();
    }

    private UserDto mapToUserDto(User user, CompanyDto companyDto) {
        UserDto userDto = userMapper.toDto(user);
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
        User savedUser = findUserOrThrow(userId);
        Long companyId = savedUser.getCompanyId();
        try {
            userRepository.deleteById(userId);
            unassignUserFromCompany(savedUser, companyId);
            log.info("User deleted successfully userId={}. User unassigned from company companyId={} successfully", userId, companyId);
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

        List<User> users = userRepository.findByCompanyId(companyId);
        if (users.isEmpty()) {
            return List.of();
        }

        CompanyDto companyDto = extractCompanyData(companyClient.getCompanyById(companyId), companyId);
        CompanyShortDto companyShortDto = new CompanyShortDto(
                companyDto.getId(),
                companyDto.getName(),
                companyDto.getBudget()
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
