package com.pii.user_service.client;

import com.pii.user_service.dto.CompanyDto;

public interface CompanyClient {

    CompanyDto getCompanyById(Long id);

}
