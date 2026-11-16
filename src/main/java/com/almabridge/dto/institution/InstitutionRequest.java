package com.almabridge.dto.institution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstitutionRequest(

    @NotBlank(message = "Institution name is required")
    @Size(max = 150)
    String institutionName,

    @NotBlank(message = "Institution code is required")
    @Size(max = 50)
    String institutionCode,

    @Size(max = 100)
    String city,

    @Size(max = 100)
    String state

) {}