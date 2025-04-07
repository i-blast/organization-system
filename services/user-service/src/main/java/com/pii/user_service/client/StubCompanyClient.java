package com.pii.user_service.client;

import com.pii.shared.dto.CompanyDto;
import com.pii.shared.dto.UserDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class StubCompanyClient implements CompanyClient {

    @Override
    public CompanyDto getCompanyById(Long id) {
        return new CompanyDto(
                id,
                "Stub Company",
                100000000L,
                List.of(
                        new UserDto(1L, "Stub1", "Doe", "+1234567890", null),
                        new UserDto(1L, "Stub2", "Doe", "+1234567890", null)
                )
        );
    }
}
