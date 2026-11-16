package com.almabridge.dto.event.registration;

import java.time.LocalDateTime;

public record EventParticipantResponse(
        Long registrationId,
        Long userId,
        String fullName,
        String email,
        LocalDateTime registeredAt
) {
}