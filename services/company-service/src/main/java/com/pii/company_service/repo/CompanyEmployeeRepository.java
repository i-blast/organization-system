package com.pii.company_service.repo;

import com.pii.company_service.entity.CompanyEmployee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployee, Long> {

    @Transactional
    @Modifying
    @Query("INSERT INTO CompanyEmployee(companyId, employeeId) VALUES (:companyId, :employeeId)")
    void assignEmployeeToCompany(Long companyId, Long employeeId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CompanyEmployee WHERE companyId = :companyId AND employeeId = :employeeId")
    void unassignEmployeeFromCompany(Long companyId, Long employeeId);

    boolean existsByCompanyIdAndEmployeeId(Long companyId, Long employeeId);

    List<CompanyEmployee> findByEmployeeIdIn(List<Long> employeeIds);

}
