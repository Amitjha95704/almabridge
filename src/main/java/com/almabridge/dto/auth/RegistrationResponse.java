
package com.almabridge.dto.auth;

public record RegistrationResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        Long institutionId,
        String message
) {}
