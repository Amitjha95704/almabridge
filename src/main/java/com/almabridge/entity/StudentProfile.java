//package com.almabridge.entity;
//
//public class StudentProfile {
//
//}

package com.almabridge.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(length = 50)
    private String rollNumber;

    @Column(length = 100)
    private String branch;

    private Integer graduationYear;

    @Column(length = 100)
    private String targetCareerDomain;

    @Column(length = 20)
    private String phoneNumber;
}