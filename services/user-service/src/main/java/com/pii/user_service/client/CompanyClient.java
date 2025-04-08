package com.pii.user_service.client;

import com.pii.shared.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "company-service",
        url = "http://localhost:8082",
        path = "/api/companies"
)
public interface CompanyClient {

    @GetMapping("/{id}")
    ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id);

    @PostMapping("/{companyId}/employees/{userId}")
    ResponseEntity<Void> assignUserToCompany(@PathVariable Long userId, @PathVariable Long companyId);

    @DeleteMapping("/{companyId}/employees/{userId}")
    ResponseEntity<Void> unassignUserFromCompany(@PathVariable Long userId, @PathVariable Long companyId);

    @PostMapping("/employees/companies")
    ResponseEntity<Map<Long, CompanyDto>> getCompaniesByUsers(@RequestBody List<Long> userIds);

}
