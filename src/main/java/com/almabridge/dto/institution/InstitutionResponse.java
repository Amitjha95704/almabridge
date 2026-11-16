package com.almabridge.dto.institution;

public record InstitutionResponse(

    Long id,
    String institutionName,
    String institutionCode,
    String city,
    String state,
    Boolean active

) {}