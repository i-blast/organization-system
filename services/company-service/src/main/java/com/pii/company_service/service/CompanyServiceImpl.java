package com.pii.company_service.service;

import com.pii.company_service.client.UserClient;
import com.pii.company_service.exception.CompanyNotFoundException;
import com.pii.company_service.mapper.CompanyMapper;
import com.pii.company_service.repo.CompanyRepository;
import com.pii.shared.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserClient userClient;

    @Override
    public CompanyDto findCompanyById(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        var companyDto = companyMapper.toDto(company);
        companyDto = fillWithUsers(companyDto);
        return companyDto;
    }

    private CompanyDto fillWithUsers(CompanyDto companyDto) {
        var users = userClient.getUsersByCompany(companyDto.id());
        return new CompanyDto(
                companyDto.id(),
                companyDto.name(),
                companyDto.budget(),
                users
        );
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        var company = companyMapper.toEntity(companyDto);
        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.setName(companyDto.name());
        company.setBudget(companyDto.budget());
        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toDto)
                .map(this::fillWithUsers)
                .toList();
    }
}
