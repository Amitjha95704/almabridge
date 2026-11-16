
package com.almabridge.dto.auth;

public record UserProfileResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        Long institutionId
) {}
