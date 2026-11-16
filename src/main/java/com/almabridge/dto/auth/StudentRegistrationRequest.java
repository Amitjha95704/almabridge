
package com.almabridge.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentRegistrationRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        @Size(max = 150)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72,
              message = "Password must contain 8 to 72 characters")
        String password,

        @NotNull(message = "Institution ID is required")
        Long institutionId,

        @NotBlank(message = "Roll number is required")
        @Size(max = 50)
        String rollNumber,

        @NotBlank(message = "Branch is required")
        @Size(max = 100)
        String branch,

        @NotNull(message = "Graduation year is required")
        @Min(2020)
        @Max(2100)
        Integer graduationYear,

        @Size(max = 100)
        String targetCareerDomain,

        @Size(max = 20)
        String phoneNumber

) {}
