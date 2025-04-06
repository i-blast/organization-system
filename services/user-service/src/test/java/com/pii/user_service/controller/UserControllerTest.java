package com.pii.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.pii.shared.util.TestDataFactory.createUserDtoWithCompany;
import static com.pii.shared.util.TestDataFactory.createUserWithName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {
        var userDto = createUserDtoWithCompany();
        when(userService.createUser(any())).thenReturn(userDto);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.lastName()));
        verify(userService).createUser(any());
    }

    @Test
    void shouldGetUserById() throws Exception {
        var userDto = createUserDtoWithCompany();
        when(userService.findUserById(1L)).thenReturn(userDto);
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
        verify(userService).findUserById(1L);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        var userDto = createUserDtoWithCompany();
        when(userService.findAllUsers()).thenReturn(List.of(userDto));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
        verify(userService).findAllUsers();
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var updatedUser = createUserWithName("Updated");
        when(userService.updateUser(eq(1L), any())).thenReturn(updatedUser);
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
        verify(userService).updateUser(eq(1L), any());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
        verify(userService).deleteUser(1L);
    }

    @Test
    void getUsersByCompany_shouldReturnUserList() throws Exception {
        var userDto = createUserDtoWithCompany();
        when(userService.findUserByCompany(1L)).thenReturn(List.of(userDto));
        mockMvc.perform(get("/api/users/by-company/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company.id").value(userDto.company().id()));
        verify(userService).findUserByCompany(1L);
    }
}
