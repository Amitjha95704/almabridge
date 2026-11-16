package com.almabridge.dto.event.registration;

import com.almabridge.enums.EventRegistrationStatus;

import java.time.LocalDateTime;

public record EventRegistrationResponse(
        Long registrationId,
        Long eventId,
        String eventTitle,
        EventRegistrationStatus status,
        LocalDateTime registeredAt,
        LocalDateTime cancelledAt
) {
}