package com.almabridge.repository;

import com.almabridge.dto.admin.analytics.BranchDistributionResponse;
import com.almabridge.dto.admin.analytics.GraduationYearDistributionResponse;
import com.almabridge.entity.StudentProfile;
import com.almabridge.enums.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//import java.awt.print.Pageable;
import java.util.Optional;

public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
    
    // Get one student belonging to the admin's institution
    @Query("""
        SELECT sp
        FROM StudentProfile sp
        JOIN FETCH sp.user u
        WHERE u.id = :userId
          AND u.institution.id = :institutionId
          AND u.role = :role
    """)
    Optional<StudentProfile> findStudentByUserAndInstitution(
            @Param("userId") Long userId,
            @Param("institutionId") Long institutionId,
            @Param("role") UserRole role
    );

    // Search and paginate students within one institution
    @Query(
        value = """
            SELECT sp
            FROM StudentProfile sp
            JOIN FETCH sp.user u
            WHERE u.institution.id = :institutionId
              AND u.role = :role
              AND (
                    :keyword IS NULL
                    OR :keyword = ''
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(sp.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(sp.branch) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
        """,
        countQuery = """
            SELECT COUNT(sp)
            FROM StudentProfile sp
            JOIN sp.user u
            WHERE u.institution.id = :institutionId
              AND u.role = :role
              AND (
                    :keyword IS NULL
                    OR :keyword = ''
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(sp.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(sp.branch) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
        """
    )
    Page<StudentProfile> searchStudents(
            @Param("institutionId") Long institutionId,
            @Param("role") UserRole role,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    
    @Query("""
    	    SELECT new com.almabridge.dto.admin.analytics.BranchDistributionResponse(
    	        COALESCE(sp.branch, 'Not specified'),
    	        COUNT(sp)
    	    )
    	    FROM StudentProfile sp
    	    JOIN sp.user u
    	    WHERE u.institution.id = :institutionId
    	      AND u.role = :role
    	    GROUP BY sp.branch
    	    ORDER BY COUNT(sp) DESC
    	""")
    	List<BranchDistributionResponse> getBranchDistribution(
    	        @Param("institutionId") Long institutionId,
    	        @Param("role") UserRole role
    	);
    
    @Query("""
    	    SELECT new com.almabridge.dto.admin.analytics.GraduationYearDistributionResponse(
    	        sp.graduationYear,
    	        COUNT(sp)
    	    )
    	    FROM StudentProfile sp
    	    JOIN sp.user u
    	    WHERE u.institution.id = :institutionId
    	      AND u.role = :role
    	    GROUP BY sp.graduationYear
    	    ORDER BY sp.graduationYear ASC
    	""")
    	List<GraduationYearDistributionResponse> getGraduationYearDistribution(
    	        @Param("institutionId") Long institutionId,
    	        @Param("role") UserRole role
    	);
    
}