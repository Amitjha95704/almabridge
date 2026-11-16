
package com.almabridge.dto.admin;

public record AdminInstitutionProfileResponse(
        Long institutionId,
        String institutionName,
        String institutionCode,
        String city,
        String state
) {}
