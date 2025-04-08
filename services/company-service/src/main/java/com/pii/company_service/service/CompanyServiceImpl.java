package com.pii.company_service.service;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.dto.CreateCompanyRequest;
import com.pii.company_service.entity.Company;
import com.pii.company_service.entity.CompanyEmployee;
import com.pii.company_service.mapper.CompanyMapper;
import com.pii.company_service.repo.CompanyEmployeeRepository;
import com.pii.company_service.repo.CompanyRepository;
import com.pii.shared.dto.CompanyDto;
import com.pii.company_service.exception.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserClient userClient;
    private final CompanyEmployeeRepository companyEmployeeRepository;

    @Override
    public CompanyDto findCompanyById(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        var companyDto = companyMapper.toDto(company);
        return companyDto;
    }

    @Override
    public CompanyDto createCompany(CreateCompanyRequest createCompanyRequest) {
        var company = companyMapper.toEntity(createCompanyRequest);
        var newCompany = companyRepository.save(company);
        return companyMapper.toDto(newCompany);
    }

    @Override
    public CompanyDto updateCompany(Long id, CreateCompanyRequest createCompanyRequest) {
        // TODO
        /*var company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.setName(createCompanyRequest.name());
        company.setBudget(createCompanyRequest.budget());
        return companyMapper.toDto(companyRepository.save(company));*/
        return null;
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        // TODO
        return List.of();
/*        return companyRepository.findAll().stream()
                .map(companyMapper::toDto)
                .map(this::fillWithUsers)
                .toList();*/
    }

    @Override
    public void assignEmployeeToCompany(Long employeeId, Long companyId) {
        companyEmployeeRepository.assignEmployeeToCompany(companyId, employeeId);
        log.info("Employee assigned to {} to company {}", employeeId, companyId);
    }

    @Override
    public void unassignEmployeeFromCompany(Long employeeId, Long companyId) {
        companyEmployeeRepository.unassignEmployeeFromCompany(companyId, employeeId);
        log.info("Employee unassigned {} from company {}", employeeId, companyId);
    }

    @Override
    public Map<Long, CompanyDto> getCompaniesByEmployees(List<Long> employeeIds) {

        var employeesInCompanies = companyEmployeeRepository.findByEmployeeIdIn(employeeIds);

        var companyIds = employeesInCompanies.stream()
                .map(CompanyEmployee::getCompanyId)
                .distinct()
                .toList();
        var companies = companyRepository.findAllById(companyIds).stream()
                .collect(Collectors.toMap(Company::getId, Function.identity()));

        return employeesInCompanies.stream()
                .collect(Collectors.toMap(
                        CompanyEmployee::getEmployeeId,
                        companyEmployee -> companyMapper.toDto(companies.get(companyEmployee.getCompanyId()))
                ));
    }
}
