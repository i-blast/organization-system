package com.pii.company_service.service;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.entity.Company;
import com.pii.company_service.entity.CompanyEmployee;
import com.pii.company_service.exception.CompanyNotFoundException;
import com.pii.company_service.mapper.CompanyMapper;
import com.pii.company_service.mapper.ToEmployeesMapping;
import com.pii.company_service.repo.CompanyEmployeeRepository;
import com.pii.company_service.repo.CompanyRepository;
import com.pii.shared.dto.CreateCompanyRequest;
import com.pii.shared.dto.UserDto;
import com.pii.shared.dto.UserShortDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.pii.company_service.util.TestDataFactory.createCompanyEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CompanyEmployeeRepository companyEmployeeRepository;
    @Mock
    private UserClient userClient;

    private CompanyMapper companyMapper;
    private ToEmployeesMapping toEmployeesMapping;
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        companyMapper = Mappers.getMapper(CompanyMapper.class);
        toEmployeesMapping = mock(ToEmployeesMapping.class);
        companyService = new CompanyServiceImpl(
                companyRepository,
                companyEmployeeRepository,
                companyMapper,
                toEmployeesMapping,
                userClient
        );
    }

    @Test
    void shouldFindCompanyById() {
        var company = createCompanyEntity();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        var result = companyService.findCompanyById(1L);
        assertThat(result.getName()).isEqualTo(company.getName());
        assertThat(result.getBudget()).isEqualTo(company.getBudget());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> companyService.findCompanyById(999L))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("Company not found.");
    }

    @Test
    void shouldCreateCompany() {
        var createRequest = new CreateCompanyRequest("Очень серьёзная организация", 100000000L, List.of(1L, 2L, 3L));
        var companyEntity = createCompanyEntity();
        companyEntity.setId(1L);
        var userShortDto = new UserShortDto(1L, "John", "Doe", "123456");
        when(companyRepository.save(any(Company.class))).thenReturn(companyEntity);
        when(toEmployeesMapping.mapCompanyToUsers(1L)).thenReturn(List.of(userShortDto));

        var result = companyService.createCompany(createRequest);

        assertThat(result.getName()).isEqualTo(companyEntity.getName());
        assertThat(result.getBudget()).isEqualTo(100000000L);
        assertThat(result.getEmployees()).containsExactly(userShortDto);
        verify(companyEmployeeRepository).assignEmployeesToCompany(companyEntity.getId(), List.of(1L, 2L, 3L));
    }

    @Test
    void shouldUpdateCompany() {
        var companyToUpdateId = 1L;
        var companyEntity = createCompanyEntity();
        companyEntity.setId(companyToUpdateId);
        var updateRequest = new CreateCompanyRequest("Upd Очень серьёзная организация", 99999999999L, List.of(1L, 2L, 3L));
        var userShortDto = new UserShortDto(1L, "Jane", "Smith", "987654");
        when(companyRepository.findById(companyToUpdateId)).thenReturn(Optional.of(companyEntity));
        when(toEmployeesMapping.mapCompanyToUsers(companyToUpdateId)).thenReturn(List.of(userShortDto));
        when(companyRepository.save(any(Company.class))).thenReturn(companyEntity);

        var result = companyService.updateCompany(companyToUpdateId, updateRequest);

        assertThat(result.getName()).isEqualTo("Upd Очень серьёзная организация");
        assertThat(result.getBudget()).isEqualTo(99999999999L);
        assertThat(result.getEmployees()).containsExactly(userShortDto);
        verify(companyEmployeeRepository).unassignAllFromCompany(companyToUpdateId);
        verify(companyEmployeeRepository).assignEmployeesToCompany(companyToUpdateId, List.of(1L, 2L, 3L));
    }

    @Test
    void shouldFindAllCompanies() {
        var employeeId = 1L;
        var companyEntity = createCompanyEntity();
        companyEntity.setId(1L);
        var employee = new CompanyEmployee(companyEntity.getId(), employeeId);
        var userShort = new UserShortDto(employeeId, "Test", "User", "000");
        when(companyRepository.findAll()).thenReturn(List.of(companyEntity));
        when(companyEmployeeRepository.findByCompanyIdIn(List.of(companyEntity.getId()))).thenReturn(List.of(employee));
        when(userClient.getEmployeesByIds(List.of(employeeId)))
                .thenReturn(ResponseEntity.ok(List.of(
                        new UserDto(employeeId, "Test", "User", "000", null)
                )));

        var result = companyService.findAllCompanies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployees()).containsExactly(userShort);
    }

    @Test
    void shouldDeleteCompany() {
        var companyEntity = createCompanyEntity();
        when(companyRepository.findById(companyEntity.getId())).thenReturn(Optional.of(companyEntity));
        companyService.deleteCompany(companyEntity.getId());
        verify(companyEmployeeRepository).unassignAllFromCompany(companyEntity.getId());
        verify(companyRepository).delete(companyEntity);
    }

    @Test
    void shouldAssignEmployeeToCompany() {
        companyService.assignEmployeeToCompany(123L, 456L);
        verify(companyEmployeeRepository).assignEmployeesToCompany(456L, List.of(123L));
    }

    @Test
    void shouldUnassignEmployeeFromCompany() {
        companyService.unassignEmployeeFromCompany(123L, 456L);
        verify(companyEmployeeRepository).unassignEmployeeFromCompany(456L, 123L);
    }

    @Test
    void shouldGetCompaniesByEmployees() {
        var companyId = 1L;
        var employeeId = 1L;
        var company = createCompanyEntity();
        company.setId(companyId);
        var assignment = new CompanyEmployee(companyId, employeeId);
        when(companyEmployeeRepository.findByEmployeeIdIn(List.of(employeeId))).thenReturn(List.of(assignment));
        when(companyRepository.findAllById(List.of(companyId))).thenReturn(List.of(company));

        var result = companyService.getCompaniesByEmployees(List.of(employeeId));

        assertThat(result).hasSize(1);
        assertThat(result.containsKey(employeeId)).isTrue();
        assertThat(result.get(employeeId).getId()).isEqualTo(company.getId());
    }
}
