package com.almabridge.repository;

import com.almabridge.entity.AlumniProfile;
import com.almabridge.enums.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.almabridge.dto.admin.analytics.AlumniCompanyDistributionResponse;

import java.util.List;


public interface AlumniProfileRepository
        extends JpaRepository<AlumniProfile, Long> {

    Optional<AlumniProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
    
    // Find one alumni belonging to the admin's institution
    @Query("""
        SELECT ap
        FROM AlumniProfile ap
        JOIN FETCH ap.user u
        WHERE u.id = :userId
          AND u.institution.id = :institutionId
          AND u.role = :role
    """)
    Optional<AlumniProfile> findAlumniByUserAndInstitution(
            @Param("userId") Long userId,
            @Param("institutionId") Long institutionId,
            @Param("role") UserRole role
    );

    // Search and paginate alumni within one institution
    @Query(
        value = """
            SELECT ap
            FROM AlumniProfile ap
            JOIN FETCH ap.user u
            WHERE u.institution.id = :institutionId
              AND u.role = :role
              AND (
                    :keyword IS NULL
                    OR :keyword = ''
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.branch) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
        """,
        countQuery = """
            SELECT COUNT(ap)
            FROM AlumniProfile ap
            JOIN ap.user u
            WHERE u.institution.id = :institutionId
              AND u.role = :role
              AND (
                    :keyword IS NULL
                    OR :keyword = ''
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.branch) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(ap.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
        """
    )
    Page<AlumniProfile> searchAlumni(
            @Param("institutionId") Long institutionId,
            @Param("role") UserRole role,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    

@Query("""
    SELECT new com.almabridge.dto.admin.analytics.AlumniCompanyDistributionResponse(
        COALESCE(ap.companyName, 'Not specified'),
        COUNT(ap)
    )
    FROM AlumniProfile ap
    JOIN ap.user u
    WHERE u.institution.id = :institutionId
      AND u.role = :role
    GROUP BY ap.companyName
    ORDER BY COUNT(ap) DESC
""")
List<AlumniCompanyDistributionResponse> getCompanyDistribution(
        @Param("institutionId") Long institutionId,
        @Param("role") UserRole role
);

}