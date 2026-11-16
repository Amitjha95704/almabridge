
package com.almabridge.repository;

import com.almabridge.entity.EventRegistration;
import com.almabridge.enums.EventRegistrationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    // Find an existing registration for a user and event
    Optional<EventRegistration> findByEventIdAndUserId(
            Long eventId,
            Long userId
    );

    // Check whether a user is currently registered
    boolean existsByEventIdAndUserIdAndStatus(
            Long eventId,
            Long userId,
            EventRegistrationStatus status
    );

    // Count active registrations for capacity validation
    long countByEventIdAndStatus(
            Long eventId,
            EventRegistrationStatus status
    );

    // Get registrations for a particular event
    Page<EventRegistration> findAllByEventIdAndStatus(
            Long eventId,
            EventRegistrationStatus status,
            Pageable pageable
    );

    // Get registrations belonging to a particular user
    Page<EventRegistration> findAllByUserIdAndStatus(
            Long userId,
            EventRegistrationStatus status,
            Pageable pageable
    );

    // Find a registration belonging to a specific user
    Optional<EventRegistration> findByIdAndUserId(
            Long registrationId,
            Long userId
    );
}
