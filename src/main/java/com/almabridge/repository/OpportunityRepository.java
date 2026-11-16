
package com.almabridge.repository;

import com.almabridge.entity.Opportunity;
import com.almabridge.enums.OpportunityStatus;
import com.almabridge.enums.OpportunityType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityRepository
        extends JpaRepository<Opportunity, Long> {

    // Find one opportunity belonging to a particular institution
    Optional<Opportunity> findByIdAndInstitutionId(
            Long id,
            Long institutionId
    );

    // Find all opportunities belonging to a particular institution
    Page<Opportunity> findAllByInstitutionId(
            Long institutionId,
            Pageable pageable
    );

    // Find opportunities by institution and status
    Page<Opportunity> findAllByInstitutionIdAndStatus(
            Long institutionId,
            OpportunityStatus status,
            Pageable pageable
    );

    // Find published opportunities visible to students
    Page<Opportunity> findAllByInstitutionIdAndStatusOrderByCreatedAtDesc(
            Long institutionId,
            OpportunityStatus status,
            Pageable pageable
    );

    // Filter opportunities by type
    Page<Opportunity> findAllByInstitutionIdAndStatusAndOpportunityType(
            Long institutionId,
            OpportunityStatus status,
            OpportunityType opportunityType,
            Pageable pageable
    );

    // Check whether an opportunity exists in a particular institution
    boolean existsByIdAndInstitutionId(
            Long id,
            Long institutionId
    );
    
    Page<Opportunity> findAllByInstitutionIdAndStatusAndOpportunityTypeOrderByCreatedAtDesc(
            Long institutionId,
            OpportunityStatus status,
            OpportunityType opportunityType,
            Pageable pageable
    );
}
