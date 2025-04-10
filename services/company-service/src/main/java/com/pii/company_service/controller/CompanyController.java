package com.pii.company_service.controller;

import com.pii.company_service.service.CompanyService;
import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.CreateCompanyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findCompanyById(id));
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.createCompany(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable Long id,
            @RequestBody @Valid CreateCompanyRequest dto
    ) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.findAllCompanies());
    }

    @PostMapping("/{companyId}/employees/{employeeId}")
    public ResponseEntity<Void> assignEmployeeToCompany(
            @PathVariable Long employeeId,
            @PathVariable Long companyId
    ) {
        companyService.assignEmployeeToCompany(employeeId, companyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{companyId}/employees/{employeeId}")
    public ResponseEntity<Void> unassignEmployeeFromCompany(
            @PathVariable Long employeeId,
            @PathVariable Long companyId
    ) {
        companyService.unassignEmployeeFromCompany(employeeId, companyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/employees/companies")
    ResponseEntity<Map<Long, CompanyDto>> getCompaniesByEmployees(
            @RequestBody List<Long> companiesByEmployees
    ) {
        return ResponseEntity.ok(companyService.getCompaniesByEmployees(companiesByEmployees));
    }
}
