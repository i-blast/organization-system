package com.pii.user_service.controller;

import com.pii.shared.dto.CreateUserRequest;
import com.pii.shared.dto.UserDto;
import com.pii.user_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("Creating user={}", createUserRequest);
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Getting user by id={}", id);
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid CreateUserRequest createUserRequest
    ) {
        log.info("Updating user with id={} and data={}", id, createUserRequest);
        return ResponseEntity.ok(userService.updateUser(id, createUserRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Getting all users");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id={}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-company/{companyId}")
    public ResponseEntity<List<UserDto>> getUsersByCompany(@PathVariable Long companyId) {
        log.info("Getting users by company id={}", companyId);
        return ResponseEntity.ok(userService.findUsersByCompany(companyId));
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<UserDto>> getUsersByIds(
            @RequestBody @NotNull(message = "Users list must not be null") List<Long> userIds
    ) {
        log.info("Getting users by ids={}", userIds);
        return ResponseEntity.ok(userService.getUsersByIds(userIds));
    }
}
