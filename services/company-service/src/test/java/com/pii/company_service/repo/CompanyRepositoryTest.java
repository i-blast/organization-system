package com.pii.company_service.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.pii.company_service.util.TestDataFactory.createCompanyEntity;
import static org.assertj.core.api.Assertions.assertThat;

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
}
