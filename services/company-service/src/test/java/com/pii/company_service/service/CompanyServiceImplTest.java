package com.pii.company_service.service;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.exception.CompanyNotFoundException;
import com.pii.company_service.mapper.CompanyMapper;
import com.pii.company_service.repo.CompanyRepository;
import com.pii.shared.dto.CompanyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.pii.company_service.util.TestDataFactory.createCompanyEntity;
import static com.pii.shared.util.TestDataFactory.createCompanyDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserClient userClient;

    private CompanyMapper companyMapper;
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        companyMapper = Mappers.getMapper(CompanyMapper.class);
        companyService = new CompanyServiceImpl(companyRepository, companyMapper, userClient);
    }

    @Test
    void shouldFindCompanyById() {
        var company = createCompanyEntity();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        var result = companyService.findCompanyById(1L);
        assertThat(result.name()).isEqualTo(company.getName());
        assertThat(result.budget()).isEqualTo(company.getBudget());
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
        var companyDto = createCompanyDto();
        var companyEntity = companyMapper.toEntity(companyDto);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);

        var result = companyService.createCompany(companyDto);

        assertThat(result.name()).isEqualTo(companyDto.name());
        assertThat(result.budget()).isEqualTo(companyDto.budget());
    }

    @Test
    void shouldUpdateCompany() {
        var existingCompany = createCompanyEntity();
        var updatedDto = new CompanyDto(1L, "UpdatedName", 2000L, List.of());
        when(companyRepository.findById(existingCompany.getId())).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = companyService.updateCompany(existingCompany.getId(), updatedDto);

        assertThat(result.name()).isEqualTo("UpdatedName");
        assertThat(result.budget()).isEqualTo(2000L);
    }

    @Test
    void shouldFindAllCompanies() {
        var company = createCompanyEntity();
        when(companyRepository.findAll()).thenReturn(List.of(company));

        var result = companyService.findAllCompanies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo(company.getName());
    }

}
