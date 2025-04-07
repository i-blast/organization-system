package com.pii.company_service.client;

import com.pii.shared.dto.UserDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class StubUserClient implements UserClient {

    @Override
    public List<UserDto> getUsersByCompany(Long companyId) {
        return List.of(
                new UserDto(1L, "John", "Doe", "+123456789", null),
                new UserDto(2L, "Jane", "Smith", "+987654321", null)
        );
    }
}
