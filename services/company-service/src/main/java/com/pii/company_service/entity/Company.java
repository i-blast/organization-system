package com.pii.company_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Budget in USD cents.
     */
    @Column(nullable = false)
    private Long budget;
}
