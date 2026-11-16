//package com.almabridge.entity;
//
//public class AluminiProfile {
//
//}

package com.almabridge.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alumni_profiles")
@Getter
@Setter
@NoArgsConstructor
public class AlumniProfile {

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

    private Integer graduationYear;

    @Column(length = 100)
    private String branch;

    @Column(length = 150)
    private String companyName;

    @Column(length = 100)
    private String designation;

    private Integer experienceYears;
}