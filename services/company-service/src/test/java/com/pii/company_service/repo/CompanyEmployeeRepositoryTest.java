package com.pii.company_service.repo;

import com.pii.company_service.entity.CompanyEmployee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class CompanyEmployeeRepositoryTest {

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Test
    void shouldAssignEmployeeSuccessfully() {
        companyEmployeeRepository.assignEmployeeToCompany(1L, 10L);
        assertTrue(companyEmployeeRepository.existsByCompanyIdAndEmployeeId(1L, 10L));
    }

    @Test
    void shouldUnassignEmployee() {
        var companyEmployee = new CompanyEmployee(1L, 10L);
        companyEmployeeRepository.save(companyEmployee);
        assertTrue(companyEmployeeRepository.existsByCompanyIdAndEmployeeId(1L, 10L));
        companyEmployeeRepository.unassignEmployeeFromCompany(1L, 10L);
        assertFalse(companyEmployeeRepository.existsByCompanyIdAndEmployeeId(1L, 10L));
    }

    @Test
    void shouldFindByEmployeeIdIn() {
        companyEmployeeRepository.saveAll(List.of(
                new CompanyEmployee(1L, 100L),
                new CompanyEmployee(2L, 101L),
                new CompanyEmployee(3L, 102L)
        ));

        var found = companyEmployeeRepository.findByEmployeeIdIn(List.of(100L, 102L));

        assertThat(found).hasSize(2);
        assertThat(found).extracting(CompanyEmployee::getEmployeeId)
                .containsExactlyInAnyOrder(100L, 102L);
    }
}
