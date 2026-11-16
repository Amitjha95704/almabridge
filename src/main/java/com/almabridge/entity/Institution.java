
package com.almabridge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "institutions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_institution_code",
            columnNames = "institution_code"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "institution_name",
            nullable = false, length = 150)
    private String institutionName;

    @Column(name = "institution_code",
            nullable = false, length = 50)
    private String institutionCode;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}