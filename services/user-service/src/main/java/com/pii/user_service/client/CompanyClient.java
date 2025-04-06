package com.pii.user_service.client;

import com.pii.shared.dto.CompanyDto;

public interface CompanyClient {

    CompanyDto getCompanyById(Long id);

}
