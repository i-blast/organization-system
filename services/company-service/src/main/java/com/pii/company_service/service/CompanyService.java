package com.pii.company_service.service;

import com.pii.shared.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);

    CompanyDto createCompany(CompanyDto companyDto);

    CompanyDto updateCompany(Long id, CompanyDto companyDto);

    void deleteCompany(Long id);

    List<CompanyDto> findAllCompanies();

}
