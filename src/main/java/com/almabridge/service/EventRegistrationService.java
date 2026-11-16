
package com.almabridge.service;

import com.almabridge.dto.event.registration.EventParticipantResponse;
import com.almabridge.dto.event.registration.EventRegistrationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRegistrationService {

    EventRegistrationResponse registerForEvent(Long eventId);

    void cancelRegistration(Long eventId);

    Page<EventRegistrationResponse> getMyRegistrations(Pageable pageable);

    Page<EventParticipantResponse> getEventParticipants(
            Long eventId,
            Pageable pageable
    );
}
