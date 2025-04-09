package com.pii.user_service.controller;

import com.pii.shared.dto.UserDto;
import com.pii.user_service.dto.CreateUserRequest;
import com.pii.user_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid CreateUserRequest createUserRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(id, createUserRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-company/{companyId}")
    public ResponseEntity<List<UserDto>> getUsersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(userService.findUsersByCompany(companyId));
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<UserDto>> getUsersByIds(
            @RequestBody @NotNull(message = "Users list must not be null") List<Long> userIds
    ) {
        return ResponseEntity.ok(userService.getUsersByIds(userIds));
    }
}
