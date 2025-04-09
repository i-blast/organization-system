package com.pii.company_service.repo;

import com.pii.company_service.entity.Company;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.pii.company_service.util.TestDataFactory.createCompanyEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldSaveAndRetrieveCompany() {
        var company = createCompanyEntity();
        var saved = companyRepository.save(company);
        var foundCompanies = companyRepository.findAll();

        assertThat(saved.getId()).isNotNull();
        assertThat(foundCompanies).hasSize(1);
        assertThat(foundCompanies.getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(company);
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        var company = Company.builder()
                .name(" ")
                .budget(100_00L)
                .build();
        assertThatThrownBy(() -> companyRepository.saveAndFlush(company))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Company name must not be blank");
    }

    @Test
    void shouldThrowWhenBudgetIsNegative() {
        var company = Company.builder()
                .name("Test Company")
                .budget(-1L)
                .build();
        assertThatThrownBy(() -> companyRepository.saveAndFlush(company))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Budget must be zero or positive");
    }
}
