package com.pii.company_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.company_service.exception.GlobalExceptionHandler;
import com.pii.company_service.service.CompanyService;
import com.pii.shared.dto.CompanyDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.pii.shared.util.TestDataFactory.createCompanyDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@Import(GlobalExceptionHandler.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateCompany() throws Exception {
        var companyDto = createCompanyDto();
        when(companyService.createCompany(any())).thenReturn(companyDto);
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(companyDto.name()))
                .andExpect(jsonPath("$.budget").value(companyDto.budget()));
        verify(companyService).createCompany(any());
    }

    @Test
    void shouldGetCompanyById() throws Exception {
        var companyDto = createCompanyDto();
        when(companyService.findCompanyById(1L)).thenReturn(companyDto);
        mockMvc.perform(get("/api/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(companyDto.name()));
        verify(companyService).findCompanyById(1L);
    }

    @Test
    void shouldGetAllCompanies() throws Exception {
        var companyDto = createCompanyDto();
        when(companyService.findAllCompanies()).thenReturn(List.of(companyDto));
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(companyDto.name()));
        verify(companyService).findAllCompanies();
    }

    @Test
    void shouldUpdateCompany() throws Exception {
        var updatedCompany = new CompanyDto(1L, "UpdatedName", 2000L, List.of());
        when(companyService.updateCompany(eq(1L), any())).thenReturn(updatedCompany);
        mockMvc.perform(put("/api/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
        verify(companyService).updateCompany(eq(1L), any());
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNoContent());
        verify(companyService).deleteCompany(1L);
    }

    @Test
    void shouldRejectInvalidCompanyCreation() throws Exception {
        var invalidDto = new CompanyDto(null, "", -100L, null);
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
        verify(companyService, never()).createCompany(any());
    }
}