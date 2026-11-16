
package com.almabridge.repository;

import com.almabridge.entity.Event;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.EventType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find an event belonging to a specific institution
    Optional<Event> findByIdAndInstitutionId(
            Long id,
            Long institutionId
    );

    // Find all events belonging to an institution
    Page<Event> findAllByInstitutionId(
            Long institutionId,
            Pageable pageable
    );

    // Find events by institution and status
    Page<Event> findAllByInstitutionIdAndStatus(
            Long institutionId,
            EventStatus status,
            Pageable pageable
    );

    // Find published events, newest first
    Page<Event> findAllByInstitutionIdAndStatusOrderByStartTimeAsc(
            Long institutionId,
            EventStatus status,
            Pageable pageable
    );

    // Filter published events by event type
    Page<Event> findAllByInstitutionIdAndStatusAndEventTypeOrderByStartTimeAsc(
            Long institutionId,
            EventStatus status,
            EventType eventType,
            Pageable pageable
    );

    // Check whether an event belongs to a specific institution
    boolean existsByIdAndInstitutionId(
            Long id,
            Long institutionId
    );
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT e
        FROM Event e
        WHERE e.id = :eventId
          AND e.institution.id = :institutionId
        """)
    Optional<Event> findByIdAndInstitutionIdForUpdate(
            @Param("eventId") Long eventId,
            @Param("institutionId") Long institutionId
    );
}
