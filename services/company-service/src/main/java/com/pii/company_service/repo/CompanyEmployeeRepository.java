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
    @Query(value = """
                INSERT INTO company_employees(company_id, employee_id)
                SELECT :companyId, unnest(array[:employeeIds])
            """, nativeQuery = true)
    void assignEmployeesToCompany(Long companyId, List<Long> employeeIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM CompanyEmployee WHERE companyId = :companyId AND employeeId = :employeeId")
    void unassignEmployeeFromCompany(Long companyId, Long employeeId);

    @Transactional
    @Modifying
    @Query("""
                DELETE FROM CompanyEmployee ce
                WHERE ce.companyId = :companyId
            """)
    void unassignAllFromCompany(Long companyId);

    List<CompanyEmployee> findByCompanyIdIn(List<Long> companyIds);

    boolean existsByCompanyIdAndEmployeeId(Long companyId, Long employeeId);

    List<CompanyEmployee> findByEmployeeIdIn(List<Long> employeeIds);

}
