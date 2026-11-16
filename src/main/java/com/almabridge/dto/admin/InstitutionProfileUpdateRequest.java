
package com.almabridge.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstitutionProfileUpdateRequest(

        @NotBlank(message = "Institution name is required")
        @Size(max = 150, message = "Institution name cannot exceed 150 characters")
        String institutionName,

        @Size(max = 100, message = "City cannot exceed 100 characters")
        String city,

        @Size(max = 100, message = "State cannot exceed 100 characters")
        String state

) {}
