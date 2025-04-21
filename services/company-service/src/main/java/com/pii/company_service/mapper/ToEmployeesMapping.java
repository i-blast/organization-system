package com.pii.company_service.mapper;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.entity.CompanyEmployee;
import com.pii.company_service.repo.CompanyEmployeeRepository;
import com.pii.shared.dto.UserDto;
import com.pii.shared.dto.UserShortDto;
import com.pii.shared.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToEmployeesMapping {

    private static final Logger log = LoggerFactory.getLogger(ToEmployeesMapping.class);

    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final UserClient userClient;

    public List<UserShortDto> mapCompanyToUsers(Long companyId) {

        List<Long> employeeIds = companyEmployeeRepository.findByCompanyIdIn(List.of(companyId)).stream()
                .map(CompanyEmployee::getEmployeeId)
                .toList();
        if (employeeIds.isEmpty()) {
            return List.of();
        }

        log.info("Fetching employeeIds data for companyId={} with employeeIds={}", companyId, employeeIds);
        try {

            ResponseEntity<List<UserDto>> employeesByIds = userClient.getEmployeesByIds(employeeIds);
            return employeesByIds.getBody().stream()
                    .map(dto -> new UserShortDto(
                            dto.getId(),
                            dto.getFirstName(),
                            dto.getLastName(),
                            dto.getPhoneNumber()
                    ))
                    .toList();
        } catch (Exception exc) {
            log.error("Failed to fetch employeeIds data companyId={} employeeIds={}", companyId, employeeIds, exc);
            throw new ExternalServiceException("Failed to fetch employeeIds data", exc);
        }
    }
}
