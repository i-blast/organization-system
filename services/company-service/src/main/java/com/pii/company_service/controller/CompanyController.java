package com.pii.company_service.controller;

import com.pii.company_service.service.CompanyService;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CreateCompanyRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(
            @PathVariable @NotNull @Positive Long id
    ) {
        log.info("Getting company with id={}", id);
        return ResponseEntity.ok(companyService.findCompanyById(id));
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CreateCompanyRequest dto) {
        log.info("Creating company={}", dto);
        return ResponseEntity.ok(companyService.createCompany(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid CreateCompanyRequest dto
    ) {
        log.info("Updating company with id={} and data={}", id, dto);
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable @NotNull @Positive Long id
    ) {
        log.info("Deleting company with id={}", id);
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        log.info("Getting all companies");
        return ResponseEntity.ok(companyService.findAllCompanies());
    }

    @PostMapping("/{companyId}/employees/{employeeId}")
    public ResponseEntity<Void> assignEmployeeToCompany(
            @PathVariable @NotNull @Positive Long employeeId,
            @PathVariable @NotNull @Positive Long companyId
    ) {
        log.info("Assigning employee to company with employeeId={} and companyId={}", employeeId, companyId);
        companyService.assignEmployeeToCompany(employeeId, companyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{companyId}/employees/{employeeId}")
    public ResponseEntity<Void> unassignEmployeeFromCompany(
            @PathVariable @NotNull @Positive Long employeeId,
            @PathVariable @NotNull @Positive Long companyId
    ) {
        log.info("Unassigning employee from company with employeeId={} and companyId={}", employeeId, companyId);
        companyService.unassignEmployeeFromCompany(employeeId, companyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/employees/companies")
    ResponseEntity<Map<Long, CompanyDto>> getCompaniesByEmployees(
            @RequestBody @NotNull List<Long> employees
    ) {
        log.info("Getting companies by employees with ids={}", employees);
        return ResponseEntity.ok(companyService.getCompaniesByEmployees(employees));
    }
}
