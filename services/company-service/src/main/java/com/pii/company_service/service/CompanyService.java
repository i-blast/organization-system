package com.pii.company_service.service;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CreateCompanyRequest;

import java.util.List;
import java.util.Map;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);

    CompanyDto createCompany(CreateCompanyRequest createCompanyRequest);

    CompanyDto updateCompany(Long id, CreateCompanyRequest createCompanyRequest);

    void deleteCompany(Long id);

    List<CompanyDto> findAllCompanies();

    void assignEmployeeToCompany(Long employeeId, Long companyId);

    void unassignEmployeeFromCompany(Long employeeId, Long companyId);

    Map<Long, CompanyDto> getCompaniesByEmployees(List<Long> usersIds);

}
