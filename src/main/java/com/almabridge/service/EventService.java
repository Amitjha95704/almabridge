
package com.almabridge.service;

import com.almabridge.dto.event.CreateEventRequest;
import com.almabridge.dto.event.EventResponse;
import com.almabridge.dto.event.UpdateEventRequest;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.EventType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request);

    EventResponse updateEvent(Long eventId, UpdateEventRequest request);

    void deleteEvent(Long eventId);

    EventResponse publishEvent(Long eventId);

    EventResponse cancelEvent(Long eventId);

    EventResponse completeEvent(Long eventId);

    Page<EventResponse> getAdminEvents(
            EventStatus status,
            Pageable pageable
    );

    Page<EventResponse> getStudentEvents(
            EventType eventType,
            Pageable pageable
    );

    EventResponse getStudentEventById(Long eventId);
}
