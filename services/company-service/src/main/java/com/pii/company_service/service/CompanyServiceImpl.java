package com.pii.company_service.service;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.dto.CreateCompanyRequest;
import com.pii.company_service.entity.Company;
import com.pii.company_service.entity.CompanyEmployee;
import com.pii.company_service.exception.CompanyNotFoundException;
import com.pii.company_service.mapper.CompanyMapper;
import com.pii.company_service.mapper.ToEmployeesMapping;
import com.pii.company_service.repo.CompanyEmployeeRepository;
import com.pii.company_service.repo.CompanyRepository;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserDto;
import com.pii.shared.dto.UserShortDto;
import com.pii.shared.exception.ExternalServiceException;
import com.pii.shared.exception.TransactionalOperationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final CompanyMapper companyMapper;
    private final ToEmployeesMapping toEmployeesMapping;
    private final UserClient userClient;

    @Override
    public CompanyDto findCompanyById(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        var companyDto = companyMapper.toDto(company, toEmployeesMapping);
        return companyDto;
    }

    @Transactional
    @Override
    public CompanyDto createCompany(CreateCompanyRequest createCompanyRequest) {
        try {
            var newCompany = companyRepository.save(companyMapper.toEntity(createCompanyRequest));
            log.info("Company created successfully id={}", newCompany.getId());

            if (!createCompanyRequest.employees().isEmpty()) {
                companyEmployeeRepository.assignEmployeesToCompany(newCompany.getId(), createCompanyRequest.employees());
                log.info("Employees ids={} assigned to company id={} successfully", createCompanyRequest.employees(), newCompany.getId());
            }

            return companyMapper.toDto(newCompany, toEmployeesMapping);
        } catch (Exception exc) {
            log.error("Failed to create company={}", createCompanyRequest, exc);
            throw new TransactionalOperationException("Failed to create company", exc);
        }
    }

    private UserShortDto toUserShortDto(UserDto dto) {
        return new UserShortDto(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber());
    }

    @Transactional
    @Override
    public CompanyDto updateCompany(Long id, CreateCompanyRequest createCompanyRequest) {

        var savedCompany = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        savedCompany.setName(createCompanyRequest.name());
        savedCompany.setBudget(createCompanyRequest.budget());

        try {
            var updatedCompany = companyRepository.save(savedCompany);
            log.info("Company updated successfully companyId={}", updatedCompany.getId());

            if (!createCompanyRequest.employees().isEmpty()) {
                companyEmployeeRepository.unassignAllFromCompany(id);
                companyEmployeeRepository.assignEmployeesToCompany(id, createCompanyRequest.employees());
                log.info("Employees ids={} reassigned to company id={} successfully", createCompanyRequest.employees(), id);
            }

            return companyMapper.toDto(updatedCompany, toEmployeesMapping);
        } catch (Exception exc) {
            log.error("Failed to update company id={} and reassign employees={}", id, createCompanyRequest.employees(), exc);
            throw new TransactionalOperationException("Failed to update company and reassign employees", exc);
        }
    }

    @Transactional
    @Override
    public void deleteCompany(Long id) {

        var company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        try {
            companyEmployeeRepository.unassignAllFromCompany(id);
            log.info("All employees unassigned from company id={}", id);

            companyRepository.delete(company);
            log.info("Company deleted successfully id={}", id);
        } catch (Exception exc) {
            log.error("Failed to delete company id={}", id, exc);
            throw new TransactionalOperationException("Failed to delete company", exc);
        }
    }

    @Override
    public List<CompanyDto> findAllCompanies() {

        var savedCompanies = companyRepository.findAll();
        if (savedCompanies.isEmpty()) {
            return List.of();
        }

        var allEmployeeAssignments = companyEmployeeRepository.findByCompanyIdIn(
                savedCompanies.stream().map(Company::getId).toList()
        );
        var allEmployeeIds = allEmployeeAssignments.stream()
                .map(CompanyEmployee::getEmployeeId)
                .distinct()
                .toList();
        var employeesByIds = fetchEmployeesData(allEmployeeIds).stream().collect(
                Collectors.toMap(UserShortDto::id, dto -> dto)
        );
        var employeesByCompanyIds = allEmployeeAssignments.stream()
                .collect(Collectors.groupingBy(
                        CompanyEmployee::getCompanyId,
                        Collectors.mapping(
                                assignment -> employeesByIds.get(assignment.getEmployeeId()),
                                Collectors.filtering(Objects::nonNull, Collectors.toList())
                        )
                ));

        return savedCompanies.stream()
                .map(savedCompany -> new CompanyDto(
                                savedCompany.getId(),
                                savedCompany.getName(),
                                savedCompany.getBudget(),
                                employeesByCompanyIds.getOrDefault(savedCompany.getId(), List.of())
                        )
                )
                .toList();
    }

    private List<UserShortDto> fetchEmployeesData(List<Long> employees) {
        List<UserShortDto> result = List.of();
        if (!employees.isEmpty()) {
            log.info("Fetching employees data ids: {}", employees);
            try {
                var employeesResponse = userClient.getEmployeesByIds(employees);
                result = employeesResponse.getBody().stream()
                        .map(this::toUserShortDto)
                        .toList();
            } catch (Exception exc) {
                log.error("Failed to receive employees data id={}", exc, employees);
                throw new ExternalServiceException("Failed to fetch employees data ids=" + employees);
            }
        }
        return result;
    }

    @Override
    public void assignEmployeeToCompany(Long employeeId, Long companyId) {
        companyEmployeeRepository.assignEmployeesToCompany(companyId, List.of(employeeId));
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
                        companyEmployee ->
                                companyMapper.toDto(
                                        companies.get(companyEmployee.getCompanyId()),
                                        toEmployeesMapping
                                )
                ));
    }
}
