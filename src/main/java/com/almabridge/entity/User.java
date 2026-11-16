////package com.almabridge.entity;
////
////public class User {
////
////}
//
//package com.almabridge.entity;
//
//import com.almabridge.enums.UserRole;
//
//import jakarta.persistence.*;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(
//    name = "users",
//    uniqueConstraints = {
//        @UniqueConstraint(
//            name = "uk_user_email",
//            columnNames = "email"
//        )
//    }
//)
//@Getter
//@Setter
//@NoArgsConstructor
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 100)
//    private String fullName;
//
//    @Column(nullable = false, length = 150)
//    private String email;
//
//    @Column(nullable = false)
//    private String passwordHash;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    private UserRole role;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "institution_id",
//                nullable = false)
//    private Institution institution;
//
//    @Column(nullable = false)
//    private Boolean enabled = true;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//}



package com.almabridge.entity;

import com.almabridge.enums.UserRole;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_email",
            columnNames = "email"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "institution_id",
        nullable = true
    )
    private Institution institution;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    private void validateInstitutionAssociation() {

        if (role == null) {
            throw new IllegalStateException(
                "User role is required."
            );
        }

        if (role == UserRole.SUPER_ADMIN
                && institution != null) {
            throw new IllegalStateException(
                "Super Admin cannot belong to an institution."
            );
        }

        if (role != UserRole.SUPER_ADMIN
                && institution == null) {
            throw new IllegalStateException(
                "Institution is required for this user role."
            );
        }
    }
}
