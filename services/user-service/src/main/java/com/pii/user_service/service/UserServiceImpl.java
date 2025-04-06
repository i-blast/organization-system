package com.pii.user_service.service;

import com.pii.user_service.client.CompanyClient;
import com.pii.shared.dto.UserDto;
import com.pii.user_service.entity.User;
import com.pii.user_service.mapper.UserMapper;
import com.pii.user_service.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CompanyClient companyClient;

    @Override
    public UserDto createUser(UserDto userDto) {
        var user = userMapper.toEntity(userDto);
        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto findUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        var userDto = userMapper.toDto(user);
        var companyDto = companyClient.getCompanyById(user.getCompanyId());

        return new UserDto(
                userDto.id(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phoneNumber(),
                companyDto
        );
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPhoneNumber(userDto.phoneNumber());
        user.setCompanyId(userDto.company().id());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    var companyDto = companyClient.getCompanyById(user.getCompanyId());
                    return new UserDto(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            companyDto
                    );
                })
                .toList();
    }

    @Override
    public List<UserDto> findUserByCompany(Long companyId) {
        return userRepository.findByCompanyId(companyId).stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        companyClient.getCompanyById(user.getCompanyId())
                ))
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
