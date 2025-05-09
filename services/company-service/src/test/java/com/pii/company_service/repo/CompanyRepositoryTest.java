package com.pii.company_service.repo;

import com.pii.company_service.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
        Company company = createCompanyEntity();
        Company saved = companyRepository.save(company);
        List<Company> foundCompanies = companyRepository.findAll();

        assertThat(saved.getId()).isNotNull();
        assertThat(foundCompanies).hasSize(1);
        assertThat(foundCompanies.getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(company);
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        Company company = Company.builder()
                .name(null)
                .budget(100_00L)
                .build();
        assertThatThrownBy(() -> companyRepository.saveAndFlush(company))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
