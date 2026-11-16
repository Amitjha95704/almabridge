
package com.almabridge.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlumniRegistrationRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        @Size(max = 150)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72)
        String password,

        @NotNull(message = "Institution ID is required")
        Long institutionId,

        @NotNull(message = "Graduation year is required")
        @Min(1950)
        @Max(2100)
        Integer graduationYear,

        @NotBlank(message = "Branch is required")
        @Size(max = 100)
        String branch,

        @NotBlank(message = "Company name is required")
        @Size(max = 150)
        String companyName,

        @NotBlank(message = "Designation is required")
        @Size(max = 100)
        String designation,

        @NotNull(message = "Experience is required")
        @Min(0)
        @Max(70)
        Integer experienceYears

) {}
