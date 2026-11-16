
package com.almabridge.dto.opportunity;

import com.almabridge.enums.OpportunityStatus;
import com.almabridge.enums.OpportunityType;
import com.almabridge.enums.WorkMode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OpportunityResponse(

        Long id,
        Long institutionId,
        String companyName,
        String jobTitle,
        OpportunityType opportunityType,
        String description,
        String eligibilityCriteria,
        String location,
        WorkMode workMode,
        String salary,
        String applicationUrl,
        LocalDate applicationDeadline,
        OpportunityStatus status,
        Long createdById,
        String createdByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
