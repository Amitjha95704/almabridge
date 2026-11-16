
package com.almabridge.service;

import com.almabridge.dto.opportunity.CreateOpportunityRequest;
import com.almabridge.dto.opportunity.OpportunityResponse;
import com.almabridge.dto.opportunity.UpdateOpportunityRequest;
import com.almabridge.enums.OpportunityStatus;
import com.almabridge.enums.OpportunityType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpportunityService {

    OpportunityResponse createOpportunity(
            CreateOpportunityRequest request
    );

    OpportunityResponse updateOpportunity(
            Long opportunityId,
            UpdateOpportunityRequest request
    );

    void deleteOpportunity(Long opportunityId);

    OpportunityResponse publishOpportunity(Long opportunityId);

    OpportunityResponse closeOpportunity(Long opportunityId);

    Page<OpportunityResponse> getAdminOpportunities(
            OpportunityStatus status,
            Pageable pageable
    );

    Page<OpportunityResponse> getStudentOpportunities(
            OpportunityType type,
            Pageable pageable
    );

    OpportunityResponse getStudentOpportunityById(Long opportunityId);
}
