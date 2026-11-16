
package com.almabridge.controller;

import com.almabridge.dto.event.CreateEventRequest;
import com.almabridge.dto.event.EventResponse;
import com.almabridge.dto.event.UpdateEventRequest;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.EventType;
import com.almabridge.service.EventService;

import jakarta.validation.Valid;

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
public class EventController {

    private final EventService eventService;

    // =====================================================
    // COLLEGE ADMIN APIs
    // =====================================================

    // Create event
    @PostMapping("/admin/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request) {

        EventResponse response = eventService.createEvent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Update event
    @PutMapping("/admin/events/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request) {

        EventResponse response =
                eventService.updateEvent(id, request);

        return ResponseEntity.ok(response);
    }

    // Delete draft event
    @DeleteMapping("/admin/events/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id) {

        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }

    // Publish event
    @PatchMapping("/admin/events/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable Long id) {

        EventResponse response =
                eventService.publishEvent(id);

        return ResponseEntity.ok(response);
    }

    // Cancel event
    @PatchMapping("/admin/events/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> cancelEvent(
            @PathVariable Long id) {

        EventResponse response =
                eventService.cancelEvent(id);

        return ResponseEntity.ok(response);
    }

    // Mark event as completed
    @PatchMapping("/admin/events/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> completeEvent(
            @PathVariable Long id) {

        EventResponse response =
                eventService.completeEvent(id);

        return ResponseEntity.ok(response);
    }

    // Get institution events with optional status filter
    @GetMapping("/admin/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EventResponse>> getAdminEvents(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "startTime")
        );

        Page<EventResponse> response =
                eventService.getAdminEvents(status, pageable);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // STUDENT AND ALUMNI APIs
    // =====================================================

    // View published events
    @GetMapping("/events")
    @PreAuthorize("hasAnyRole('STUDENT', 'ALUMNI')")
    public ResponseEntity<Page<EventResponse>> getPublishedEvents(
            @RequestParam(required = false) EventType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "startTime")
        );

        Page<EventResponse> response =
                eventService.getStudentEvents(type, pageable);

        return ResponseEntity.ok(response);
    }

    // View published event details
    @GetMapping("/events/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ALUMNI')")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable Long id) {

        EventResponse response =
                eventService.getStudentEventById(id);

        return ResponseEntity.ok(response);
    }
}
