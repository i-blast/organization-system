package com.pii.company_service.client;

import com.pii.shared.dto.UserDto;

import java.util.List;

public interface UserClient {

    List<UserDto> getUsersByCompany(Long companyId);
}
