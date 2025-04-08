package com.pii.company_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CompanyEmployeeId.class)
public class CompanyEmployee {

    @Id
    private Long companyId;

    @Id
    private Long employeeId;
}

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
