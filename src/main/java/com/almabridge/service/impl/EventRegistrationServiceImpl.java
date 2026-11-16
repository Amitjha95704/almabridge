
package com.almabridge.service.impl;

import com.almabridge.dto.event.registration.EventParticipantResponse;
import com.almabridge.dto.event.registration.EventRegistrationResponse;
import com.almabridge.entity.Event;
import com.almabridge.entity.EventRegistration;
import com.almabridge.entity.User;
import com.almabridge.enums.EventRegistrationStatus;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.UserRole;
import com.almabridge.repository.EventRegistrationRepository;
import com.almabridge.repository.EventRepository;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
import com.almabridge.service.EventRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public EventRegistrationResponse registerForEvent(Long eventId) {

        validateStudentOrAlumni();

        Long institutionId = getRequiredInstitutionId();
        Long userId = currentUserService.getUserId();

        // Lock the event row to prevent concurrent overbooking.
        Event event = eventRepository
                .findByIdAndInstitutionIdForUpdate(eventId, institutionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Event not found")
                );

        validateEventForRegistration(event);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found")
                );

        EventRegistration registration = registrationRepository
                .findByEventIdAndUserId(eventId, userId)
                .orElse(null);

        if (registration != null) {

            if (registration.getStatus() ==
                    EventRegistrationStatus.REGISTERED) {
                throw new IllegalStateException(
                        "You are already registered for this event"
                );
            }

            // Reactivate an earlier cancelled registration.
            checkEventCapacity(event);

            registration.setStatus(EventRegistrationStatus.REGISTERED);
            registration.setRegisteredAt(LocalDateTime.now());
            registration.setCancelledAt(null);

        } else {

            checkEventCapacity(event);

            registration = new EventRegistration();
            registration.setEvent(event);
            registration.setUser(user);
            registration.setStatus(EventRegistrationStatus.REGISTERED);
            registration.setRegisteredAt(LocalDateTime.now());
            registration.setCancelledAt(null);
        }

        EventRegistration saved = registrationRepository.save(registration);

        return mapToRegistrationResponse(saved);
    }

    @Override
    @Transactional
    public void cancelRegistration(Long eventId) {

        validateStudentOrAlumni();

        Long institutionId = getRequiredInstitutionId();
        Long userId = currentUserService.getUserId();

        Event event = eventRepository
                .findByIdAndInstitutionIdForUpdate(eventId, institutionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Event not found")
                );

        if (event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "Registration cannot be cancelled after the event has started"
            );
        }

        EventRegistration registration = registrationRepository
                .findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "You are not registered for this event"
                        )
                );

        if (registration.getStatus() ==
                EventRegistrationStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Your registration is already cancelled"
            );
        }

        registration.setStatus(EventRegistrationStatus.CANCELLED);
        registration.setCancelledAt(LocalDateTime.now());

        registrationRepository.save(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventRegistrationResponse> getMyRegistrations(
            Pageable pageable
    ) {

        validateStudentOrAlumni();

        Long userId = currentUserService.getUserId();

        return registrationRepository
                .findAllByUserIdAndStatus(
                        userId,
                        EventRegistrationStatus.REGISTERED,
                        pageable
                )
                .map(this::mapToRegistrationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventParticipantResponse> getEventParticipants(
            Long eventId,
            Pageable pageable
    ) {

        Long institutionId = getRequiredInstitutionId();

        // Verify that this event belongs to the current institution.
        eventRepository.findByIdAndInstitutionId(eventId, institutionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Event not found")
                );

        return registrationRepository
                .findAllByEventIdAndStatus(
                        eventId,
                        EventRegistrationStatus.REGISTERED,
                        pageable
                )
                .map(this::mapToParticipantResponse);
    }

    private void validateEventForRegistration(Event event) {

        LocalDateTime now = LocalDateTime.now();

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Registration is available only for published events"
            );
        }

        if (event.getStartTime().isBefore(now)) {
            throw new IllegalStateException(
                    "Registration is closed because the event has started"
            );
        }

        if (event.getRegistrationDeadline() != null
                && !event.getRegistrationDeadline().isAfter(now)) {
            throw new IllegalStateException(
                    "The registration deadline has passed"
            );
        }
    }

    private void checkEventCapacity(Event event) {

        Integer capacity = event.getCapacity();

        // A null capacity means unlimited registrations.
        if (capacity == null) {
            return;
        }

        long registeredCount = registrationRepository
                .countByEventIdAndStatus(
                        event.getId(),
                        EventRegistrationStatus.REGISTERED
                );

        if (registeredCount >= capacity) {
            throw new IllegalStateException(
                    "Sorry, this event has reached its registration capacity"
            );
        }
    }

    private void validateStudentOrAlumni() {

        UserRole role = currentUserService.getRole();

        if (role != UserRole.STUDENT && role != UserRole.ALUMNI) {
            throw new IllegalStateException(
                    "Only students and alumni can register for events"
            );
        }
    }

    private Long getRequiredInstitutionId() {

        Long institutionId = currentUserService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Your account is not associated with an institution"
            );
        }

        return institutionId;
    }

    private EventRegistrationResponse mapToRegistrationResponse(
            EventRegistration registration
    ) {

        return new EventRegistrationResponse(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getStatus(),
                registration.getRegisteredAt(),
                registration.getCancelledAt()
        );
    }

    private EventParticipantResponse mapToParticipantResponse(
            EventRegistration registration
    ) {

        User user = registration.getUser();

        return new EventParticipantResponse(
                registration.getId(),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                registration.getRegisteredAt()
        );
    }
}
