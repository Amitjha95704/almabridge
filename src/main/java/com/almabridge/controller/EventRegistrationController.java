
package com.almabridge.controller;

import com.almabridge.dto.event.registration.EventParticipantResponse;
import com.almabridge.dto.event.registration.EventRegistrationResponse;
import com.almabridge.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final EventRegistrationService registrationService;

    // Student or Alumni registers for an event.
    @PostMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasAnyRole('STUDENT', 'ALUMNI')")
    public ResponseEntity<EventRegistrationResponse> registerForEvent(
            @PathVariable Long eventId
    ) {

        EventRegistrationResponse response =
                registrationService.registerForEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Student or Alumni cancels their registration.
    @DeleteMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasAnyRole('STUDENT', 'ALUMNI')")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable Long eventId
    ) {

        registrationService.cancelRegistration(eventId);

        return ResponseEntity.noContent().build();
    }

    // View the current user's active registrations.
    @GetMapping("/events/my-registrations")
    @PreAuthorize("hasAnyRole('STUDENT', 'ALUMNI')")
    public ResponseEntity<Page<EventRegistrationResponse>> getMyRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "registeredAt")
        );

        return ResponseEntity.ok(
                registrationService.getMyRegistrations(pageable)
        );
    }

    // Institution Admin views registered participants.
    @GetMapping("/admin/events/{eventId}/registrations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EventParticipantResponse>> getEventParticipants(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "registeredAt")
        );

        return ResponseEntity.ok(
                registrationService.getEventParticipants(
                        eventId,
                        pageable
                )
        );
    }
}
