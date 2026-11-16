
package com.almabridge.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String fullName,
        String email,
        String role,
        Long institutionId
) {}
