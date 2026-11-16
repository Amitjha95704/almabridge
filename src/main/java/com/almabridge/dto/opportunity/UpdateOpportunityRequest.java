
package com.almabridge.dto.opportunity;

import com.almabridge.enums.OpportunityType;
import com.almabridge.enums.WorkMode;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateOpportunityRequest(

        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name cannot exceed 150 characters")
        String companyName,

        @NotBlank(message = "Job title is required")
        @Size(max = 150, message = "Job title cannot exceed 150 characters")
        String jobTitle,

        @NotNull(message = "Opportunity type is required")
        OpportunityType opportunityType,

        @Size(max = 5000, message = "Description cannot exceed 5000 characters")
        String description,

        @Size(max = 5000, message = "Eligibility criteria cannot exceed 5000 characters")
        String eligibilityCriteria,

        @Size(max = 100, message = "Location cannot exceed 100 characters")
        String location,

        @NotNull(message = "Work mode is required")
        WorkMode workMode,

        @Size(max = 100, message = "Salary cannot exceed 100 characters")
        String salary,

        @Size(max = 500, message = "Application URL cannot exceed 500 characters")
        String applicationUrl,

        @Future(message = "Application deadline must be a future date")
        LocalDate applicationDeadline

) {
}
