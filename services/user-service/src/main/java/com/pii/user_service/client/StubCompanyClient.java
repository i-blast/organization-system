package com.pii.user_service.client;

import com.pii.user_service.dto.CompanyDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class StubCompanyClient implements CompanyClient {

    @Override
    public CompanyDto getCompanyById(Long id) {
        return new CompanyDto(id, "Stub Company", 100_000L, List.of(1L, 2L));
    }
}
