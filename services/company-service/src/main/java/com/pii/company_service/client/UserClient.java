package com.pii.company_service.client;

import com.pii.shared.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "user-service",
        path = "/api/users"
)
public interface UserClient {

    @GetMapping("/by-company/{companyId}")
    ResponseEntity<List<UserDto>> getUsersByCompany(@PathVariable Long companyId);

}
