package com.pii.company_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.company_service.service.CompanyService;
import com.pii.shared.dto.CreateCompanyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static com.pii.shared.util.TestDataFactory.createCompanyDto;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetCompanyById() throws Exception {
        var companyDto = createCompanyDto();
        when(companyService.findCompanyById(1L)).thenReturn(companyDto);
        mockMvc.perform(get("/api/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyDto.getId()))
                .andExpect(jsonPath("$.name").value(companyDto.getName()));
        verify(companyService).findCompanyById(1L);
    }

    @Test
    void shouldAssignEmployeeToCompany() throws Exception {
        mockMvc.perform(post("/api/companies/10/employees/5"))
                .andExpect(status().isNoContent());
        verify(companyService).assignEmployeeToCompany(5L, 10L);
    }

    @Test
    void shouldUnassignEmployeeFromCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/10/employees/5"))
                .andExpect(status().isNoContent());
        verify(companyService).unassignEmployeeFromCompany(5L, 10L);
    }

    @Test
    void shouldGetCompaniesByEmployees() throws Exception {
        var companyDto = createCompanyDto();
        var ids = List.of(1L, 2L);
        var responseMap = Map.of(1L, companyDto);
        when(companyService.getCompaniesByEmployees(ids)).thenReturn(responseMap);

        mockMvc.perform(post("/api/companies/employees/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1'].id").value(companyDto.getId()))
                .andExpect(jsonPath("$.['1'].name").value(companyDto.getName()));
        verify(companyService).getCompaniesByEmployees(ids);
    }

    @Test
    void shouldCreateCompany() throws Exception {
        var createRequest = new CreateCompanyRequest("TestCompany", 1_000_000L, List.of(1L));
        var companyDto = createCompanyDto();
        when(companyService.createCompany(createRequest)).thenReturn(companyDto);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyDto.getId()))
                .andExpect(jsonPath("$.name").value(companyDto.getName()));
        verify(companyService).createCompany(createRequest);
    }

    @Test
    void shouldUpdateCompany() throws Exception {
        var updateRequest = new CreateCompanyRequest("UpdatedCompany", 2_000_000L, List.of(2L));
        var companyDto = createCompanyDto();
        when(companyService.updateCompany(1L, updateRequest)).thenReturn(companyDto);

        mockMvc.perform(put("/api/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyDto.getId()))
                .andExpect(jsonPath("$.name").value(companyDto.getName()));
        verify(companyService).updateCompany(1L, updateRequest);
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNoContent());
        verify(companyService).deleteCompany(1L);
    }

    @Test
    void shouldGetAllCompanies() throws Exception {
        var companyDto = createCompanyDto();
        when(companyService.findAllCompanies()).thenReturn(List.of(companyDto));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(companyDto.getId()))
                .andExpect(jsonPath("$[0].name").value(companyDto.getName()));
        verify(companyService).findAllCompanies();
    }
}