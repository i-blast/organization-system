package com.pii.company_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company_employees")
@IdClass(CompanyEmployeeId.class)
public class CompanyEmployee {

    @Id
    @Column(nullable = false)
    private Long companyId;

    @Id
    @Column(nullable = false)
    private Long employeeId;
}

@NoArgsConstructor
@AllArgsConstructor
class CompanyEmployeeId implements Serializable {

    private Long companyId;

    private Long employeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEmployeeId that = (CompanyEmployeeId) o;
        return Objects.equals(companyId, that.companyId) &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, employeeId);
    }
}
