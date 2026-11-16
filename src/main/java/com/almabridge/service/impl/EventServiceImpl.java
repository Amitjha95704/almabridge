//
//package com.almabridge.service.impl;
//
//import com.almabridge.dto.event.CreateEventRequest;
//import com.almabridge.dto.event.EventResponse;
//import com.almabridge.dto.event.UpdateEventRequest;
//import com.almabridge.entity.Event;
//import com.almabridge.entity.Institution;
//import com.almabridge.entity.User;
//import com.almabridge.enums.EventMode;
//import com.almabridge.enums.EventRegistrationStatus;
//import com.almabridge.enums.EventStatus;
//import com.almabridge.enums.EventType;
//import com.almabridge.exception.ResourceNotFoundException;
//import com.almabridge.repository.EventRegistrationRepository;
//import com.almabridge.repository.EventRepository;
//import com.almabridge.repository.UserRepository;
//import com.almabridge.service.CurrentUserService;
//import com.almabridge.service.EventService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.net.URI;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//public class EventServiceImpl implements EventService {
//
//    private final EventRepository eventRepository;
//    private final UserRepository userRepository;
//    private final CurrentUserService currentUserService;
//    private final EventRegistrationRepository registrationRepository;
//
//    @Override
//    @Transactional
//    public EventResponse createEvent(CreateEventRequest request) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        User admin = userRepository.findById(currentUserService.getUserId())
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Admin user not found"));
//
//        Event event = new Event();
//
//        event.setInstitution(admin.getInstitution());
//        event.setCreatedBy(admin);
//
//        applyRequest(event, request);
//
//        event.setStatus(EventStatus.DRAFT);
//
//        Event savedEvent = eventRepository.save(event);
//
//        return mapToResponse(savedEvent);
//    }
//
//    @Override
//    @Transactional
//    public EventResponse updateEvent(
//            Long eventId,
//            UpdateEventRequest request) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = getAdminEvent(eventId, institutionId);
//
//        if (event.getStatus() == EventStatus.CANCELLED
//                || event.getStatus() == EventStatus.COMPLETED) {
//
//            throw new IllegalArgumentException(
//                    "Cancelled or completed events cannot be updated");
//        }
//
//        applyRequest(event, request);
//
//        Event updatedEvent = eventRepository.save(event);
//
//        return mapToResponse(updatedEvent);
//    }
//
//    @Override
//    @Transactional
//    public void deleteEvent(Long eventId) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = getAdminEvent(eventId, institutionId);
//
//        if (event.getStatus() != EventStatus.DRAFT) {
//            throw new IllegalArgumentException(
//                    "Only draft events can be deleted");
//        }
//
//        eventRepository.delete(event);
//    }
//
//    @Override
//    @Transactional
//    public EventResponse publishEvent(Long eventId) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = getAdminEvent(eventId, institutionId);
//
//        if (event.getStatus() == EventStatus.CANCELLED
//                || event.getStatus() == EventStatus.COMPLETED) {
//
//            throw new IllegalArgumentException(
//                    "Cancelled or completed events cannot be published");
//        }
//
//        validatePublishableEvent(event);
//
//        event.setStatus(EventStatus.PUBLISHED);
//
//        Event savedEvent = eventRepository.save(event);
//
//        return mapToResponse(savedEvent);
//    }
//
//    @Override
//    @Transactional
//    public EventResponse cancelEvent(Long eventId) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = getAdminEvent(eventId, institutionId);
//
//        if (event.getStatus() == EventStatus.COMPLETED) {
//            throw new IllegalArgumentException(
//                    "Completed events cannot be cancelled");
//        }
//
//        event.setStatus(EventStatus.CANCELLED);
//
//        Event savedEvent = eventRepository.save(event);
//
//        return mapToResponse(savedEvent);
//    }
//
//    @Override
//    @Transactional
//    public EventResponse completeEvent(Long eventId) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = getAdminEvent(eventId, institutionId);
//
//        if (event.getStatus() != EventStatus.PUBLISHED) {
//            throw new IllegalArgumentException(
//                    "Only published events can be marked as completed");
//        }
//
//        if (event.getEndTime().isAfter(LocalDateTime.now())) {
//            throw new IllegalArgumentException(
//                    "An event cannot be completed before its end time");
//        }
//
//        event.setStatus(EventStatus.COMPLETED);
//
//        Event savedEvent = eventRepository.save(event);
//
//        return mapToResponse(savedEvent);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<EventResponse> getAdminEvents(
//            EventStatus status,
//            Pageable pageable) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Page<Event> events;
//
//        if (status == null) {
//            events = eventRepository.findAllByInstitutionId(
//                    institutionId,
//                    pageable
//            );
//        } else {
//            events = eventRepository.findAllByInstitutionIdAndStatus(
//                    institutionId,
//                    status,
//                    pageable
//            );
//        }
//
//        return events.map(this::mapToResponse);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<EventResponse> getStudentEvents(
//            EventType eventType,
//            Pageable pageable) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Page<Event> events;
//
//        if (eventType == null) {
//            events = eventRepository
//                    .findAllByInstitutionIdAndStatusOrderByStartTimeAsc(
//                            institutionId,
//                            EventStatus.PUBLISHED,
//                            pageable
//                    );
//        } else {
//            events = eventRepository
//                    .findAllByInstitutionIdAndStatusAndEventTypeOrderByStartTimeAsc(
//                            institutionId,
//                            EventStatus.PUBLISHED,
//                            eventType,
//                            pageable
//                    );
//        }
//
//        return events.map(this::mapToResponse);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public EventResponse getStudentEventById(Long eventId) {
//
//        Long institutionId = getRequiredInstitutionId();
//
//        Event event = eventRepository
//                .findByIdAndInstitutionId(eventId, institutionId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Event not found"));
//
//        if (event.getStatus() != EventStatus.PUBLISHED) {
//            throw new ResourceNotFoundException("Event not found");
//        }
//
//        return mapToResponse(event);
//    }
//
//    private Long getRequiredInstitutionId() {
//
//        Long institutionId = currentUserService.getInstitutionId();
//
//        if (institutionId == null) {
//            throw new IllegalStateException(
//                    "Your account is not associated with an institution");
//        }
//
//        return institutionId;
//    }
//
//    private Event getAdminEvent(Long eventId, Long institutionId) {
//
//        return eventRepository
//                .findByIdAndInstitutionId(eventId, institutionId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Event not found"));
//    }
//
//    private void applyRequest(
//            Event event,
//            CreateEventRequest request) {
//
//        event.setTitle(request.title().trim());
//        event.setEventType(request.eventType());
//        event.setDescription(request.description());
//        event.setStartDate(request.startDate());
//        event.setEndDate(request.endDate());
//        event.setStartTime(request.startTime());
//        event.setEndTime(request.endTime());
//        event.setEventMode(request.eventMode());
//        event.setVenue(request.venue());
//        event.setMeetingUrl(validateMeetingUrl(request.meetingUrl()));
//        event.setCapacity(request.capacity());
//        event.setRegistrationDeadline(request.registrationDeadline());
//
//        validateEventDetails(
//                request.startDate(),
//                request.endDate(),
//                request.startTime(),
//                request.endTime(),
//                request.eventMode(),
//                request.venue(),
//                request.meetingUrl(),
//                request.registrationDeadline()
//        );
//    }
//
//    private void applyRequest(
//            Event event,
//            UpdateEventRequest request) {
//
//        event.setTitle(request.title().trim());
//        event.setEventType(request.eventType());
//        event.setDescription(request.description());
//        event.setStartDate(request.startDate());
//        event.setEndDate(request.endDate());
//        event.setStartTime(request.startTime());
//        event.setEndTime(request.endTime());
//        event.setEventMode(request.eventMode());
//        event.setVenue(request.venue());
//        event.setMeetingUrl(validateMeetingUrl(request.meetingUrl()));
//        event.setCapacity(request.capacity());
//        event.setRegistrationDeadline(request.registrationDeadline());
//
//        validateEventDetails(
//                request.startDate(),
//                request.endDate(),
//                request.startTime(),
//                request.endTime(),
//                request.eventMode(),
//                request.venue(),
//                request.meetingUrl(),
//                request.registrationDeadline()
//        );
//    }
//
//    private void validateEventDetails(
//            LocalDate startDate,
//            LocalDate endDate,
//            LocalDateTime startTime,
//            LocalDateTime endTime,
//            EventMode eventMode,
//            String venue,
//            String meetingUrl,
//            LocalDateTime registrationDeadline) {
//
//        if (endDate.isBefore(startDate)) {
//            throw new IllegalArgumentException(
//                    "Event end date cannot be before start date");
//        }
//
//        if (!endTime.isAfter(startTime)) {
//            throw new IllegalArgumentException(
//                    "Event end time must be after start time");
//        }
//
//        if (registrationDeadline != null
//                && !registrationDeadline.isBefore(startTime)) {
//            throw new IllegalArgumentException(
//                    "Registration deadline must be before event start time");
//        }
//
//        if (eventMode == EventMode.ONLINE
//                && (meetingUrl == null || meetingUrl.isBlank())) {
//            throw new IllegalArgumentException(
//                    "Meeting URL is required for online events");
//        }
//
//        if (eventMode == EventMode.ONSITE
//                && (venue == null || venue.isBlank())) {
//            throw new IllegalArgumentException(
//                    "Venue is required for onsite events");
//        }
//    }
//
//    private void validatePublishableEvent(Event event) {
//
//        if (event.getEndTime().isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException(
//                    "Past events cannot be published");
//        }
//
//        if (event.getRegistrationDeadline() != null
//                && !event.getRegistrationDeadline().isAfter(LocalDateTime.now())) {
//            throw new IllegalArgumentException(
//                    "Registration deadline has expired");
//        }
//    }
//
//    private String validateMeetingUrl(String meetingUrl) {
//
//        if (meetingUrl == null || meetingUrl.isBlank()) {
//            return null;
//        }
//
//        try {
//            URI uri = URI.create(meetingUrl.trim());
//
//            String scheme = uri.getScheme();
//
//            if (scheme == null
//                    || !(scheme.equalsIgnoreCase("http")
//                    || scheme.equalsIgnoreCase("https"))
//                    || uri.getHost() == null) {
//
//                throw new IllegalArgumentException();
//            }
//
//            return uri.toString();
//
//        } catch (IllegalArgumentException exception) {
//            throw new IllegalArgumentException(
//                    "Meeting URL must be a valid HTTP or HTTPS URL");
//        }
//    }
//
//    private EventResponse mapToResponse(Event event) {
//
//        Institution institution = event.getInstitution();
//        User creator = event.getCreatedBy();
//
//        long registeredCount = registrationRepository.countByEventIdAndStatus(
//                event.getId(),
//                EventRegistrationStatus.REGISTERED
//        );
//
//        Integer capacity = event.getCapacity();
//
//        Long availableSeats = capacity == null
//                ? null
//                : Math.max(0L, capacity.longValue() - registeredCount);
//
//        LocalDateTime now = LocalDateTime.now();
//
//        boolean registrationOpen =
//                event.getStatus() == EventStatus.PUBLISHED
//                && !event.getStartTime().isBefore(now)
//                && (event.getRegistrationDeadline() == null
//                    || event.getRegistrationDeadline().isAfter(now))
//                && (capacity == null || registeredCount < capacity);
//
//        return new EventResponse(
//                event.getId(),
//                institution.getId(),
//                event.getTitle(),
//                event.getEventType(),
//                event.getDescription(),
//                event.getStartDate(),
//                event.getEndDate(),
//                event.getStartTime(),
//                event.getEndTime(),
//                event.getEventMode(),
//                event.getVenue(),
//                event.getMeetingUrl(),
//                capacity,
//                event.getRegistrationDeadline(),
//                event.getStatus(),
//
//                registeredCount,
//                availableSeats,
//                registrationOpen,
//
//                creator.getId(),
//                creator.getFullName(),
//                event.getCreatedAt(),
//                event.getUpdatedAt()
//        );
//    }
//    }
//}


package com.almabridge.service.impl;

import com.almabridge.dto.event.CreateEventRequest;
import com.almabridge.dto.event.EventResponse;
import com.almabridge.dto.event.UpdateEventRequest;

import com.almabridge.entity.Event;
import com.almabridge.entity.Institution;
import com.almabridge.entity.User;

import com.almabridge.enums.EventMode;
import com.almabridge.enums.EventRegistrationStatus;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.EventType;

import com.almabridge.exception.ResourceNotFoundException;

import com.almabridge.repository.EventRegistrationRepository;
import com.almabridge.repository.EventRepository;
import com.almabridge.repository.UserRepository;

import com.almabridge.service.CurrentUserService;
import com.almabridge.service.EventService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    // =========================================================
    // CREATE EVENT
    // =========================================================

    @Override
    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {

        Long institutionId = getRequiredInstitutionId();

        User admin = userRepository.findById(currentUserService.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin user not found"));

        if (admin.getInstitution() == null
                || !institutionId.equals(admin.getInstitution().getId())) {
            throw new IllegalStateException(
                    "Admin is not associated with the current institution");
        }

        Event event = new Event();

        event.setInstitution(admin.getInstitution());
        event.setCreatedBy(admin);

        applyRequest(event, request);

        event.setStatus(EventStatus.DRAFT);

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    // =========================================================
    // UPDATE EVENT
    // =========================================================

    @Override
    @Transactional
    public EventResponse updateEvent(
            Long eventId,
            UpdateEventRequest request) {

        Long institutionId = getRequiredInstitutionId();

        Event event = getAdminEvent(eventId, institutionId);

        if (event.getStatus() == EventStatus.CANCELLED
                || event.getStatus() == EventStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Cancelled or completed events cannot be updated");
        }

        applyRequest(event, request);

        Event updatedEvent = eventRepository.save(event);

        return mapToResponse(updatedEvent);
    }

    // =========================================================
    // DELETE EVENT
    // =========================================================

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {

        Long institutionId = getRequiredInstitutionId();

        Event event = getAdminEvent(eventId, institutionId);

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalArgumentException(
                    "Only draft events can be deleted");
        }

        eventRepository.delete(event);
    }

    // =========================================================
    // PUBLISH EVENT
    // =========================================================

    @Override
    @Transactional
    public EventResponse publishEvent(Long eventId) {

        Long institutionId = getRequiredInstitutionId();

        Event event = getAdminEvent(eventId, institutionId);

        if (event.getStatus() == EventStatus.CANCELLED
                || event.getStatus() == EventStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Cancelled or completed events cannot be published");
        }

        validatePublishableEvent(event);

        event.setStatus(EventStatus.PUBLISHED);

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    // =========================================================
    // CANCEL EVENT
    // =========================================================

    @Override
    @Transactional
    public EventResponse cancelEvent(Long eventId) {

        Long institutionId = getRequiredInstitutionId();

        Event event = getAdminEvent(eventId, institutionId);

        if (event.getStatus() == EventStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Completed events cannot be cancelled");
        }

        event.setStatus(EventStatus.CANCELLED);

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    // =========================================================
    // COMPLETE EVENT
    // =========================================================

    @Override
    @Transactional
    public EventResponse completeEvent(Long eventId) {

        Long institutionId = getRequiredInstitutionId();

        Event event = getAdminEvent(eventId, institutionId);

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalArgumentException(
                    "Only published events can be marked as completed");
        }

        if (event.getEndTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "An event cannot be completed before its end time");
        }

        event.setStatus(EventStatus.COMPLETED);

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    // =========================================================
    // ADMIN EVENT LIST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getAdminEvents(
            EventStatus status,
            Pageable pageable) {

        Long institutionId = getRequiredInstitutionId();

        Page<Event> events;

        if (status == null) {

            events = eventRepository.findAllByInstitutionId(
                    institutionId,
                    pageable
            );

        } else {

            events = eventRepository.findAllByInstitutionIdAndStatus(
                    institutionId,
                    status,
                    pageable
            );
        }

        return events.map(this::mapToResponse);
    }

    // =========================================================
    // STUDENT AND ALUMNI EVENT LIST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getStudentEvents(
            EventType eventType,
            Pageable pageable) {

        Long institutionId = getRequiredInstitutionId();

        Page<Event> events;

        if (eventType == null) {

            events = eventRepository
                    .findAllByInstitutionIdAndStatusOrderByStartTimeAsc(
                            institutionId,
                            EventStatus.PUBLISHED,
                            pageable
                    );

        } else {

            events = eventRepository
                    .findAllByInstitutionIdAndStatusAndEventTypeOrderByStartTimeAsc(
                            institutionId,
                            EventStatus.PUBLISHED,
                            eventType,
                            pageable
                    );
        }

        return events.map(this::mapToResponse);
    }

    // =========================================================
    // STUDENT AND ALUMNI EVENT DETAILS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public EventResponse getStudentEventById(Long eventId) {

        Long institutionId = getRequiredInstitutionId();

        Event event = eventRepository
                .findByIdAndInstitutionId(eventId, institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Event not found");
        }

        return mapToResponse(event);
    }

    // =========================================================
    // INSTITUTION VALIDATION
    // =========================================================

    private Long getRequiredInstitutionId() {

        Long institutionId = currentUserService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Your account is not associated with an institution");
        }

        return institutionId;
    }

    // =========================================================
    // GET INSTITUTION-SCOPED EVENT
    // =========================================================

    private Event getAdminEvent(Long eventId, Long institutionId) {

        return eventRepository
                .findByIdAndInstitutionId(eventId, institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));
    }

    // =========================================================
    // APPLY CREATE REQUEST
    // =========================================================

    private void applyRequest(
            Event event,
            CreateEventRequest request) {

        validateEventDetails(
                request.startDate(),
                request.endDate(),
                request.startTime(),
                request.endTime(),
                request.eventMode(),
                request.venue(),
                request.meetingUrl(),
                request.registrationDeadline()
        );

        event.setTitle(request.title().trim());
        event.setEventType(request.eventType());
        event.setDescription(request.description());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setEventMode(request.eventMode());
        event.setVenue(request.venue());
        event.setMeetingUrl(validateMeetingUrl(request.meetingUrl()));
        event.setCapacity(request.capacity());
        event.setRegistrationDeadline(request.registrationDeadline());
    }

    // =========================================================
    // APPLY UPDATE REQUEST
    // =========================================================

    private void applyRequest(
            Event event,
            UpdateEventRequest request) {

        validateEventDetails(
                request.startDate(),
                request.endDate(),
                request.startTime(),
                request.endTime(),
                request.eventMode(),
                request.venue(),
                request.meetingUrl(),
                request.registrationDeadline()
        );

        validateCapacityUpdate(event, request.capacity());

        event.setTitle(request.title().trim());
        event.setEventType(request.eventType());
        event.setDescription(request.description());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setEventMode(request.eventMode());
        event.setVenue(request.venue());
        event.setMeetingUrl(validateMeetingUrl(request.meetingUrl()));
        event.setCapacity(request.capacity());
        event.setRegistrationDeadline(request.registrationDeadline());
    }

    // =========================================================
    // EVENT DETAILS VALIDATION
    // =========================================================

    private void validateEventDetails(
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime startTime,
            LocalDateTime endTime,
            EventMode eventMode,
            String venue,
            String meetingUrl,
            LocalDateTime registrationDeadline) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Event end date cannot be before start date");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "Event end time must be after start time");
        }

        if (!startDate.equals(startTime.toLocalDate())) {
            throw new IllegalArgumentException(
                    "Event start date must match the date in event start time");
        }

        if (!endDate.equals(endTime.toLocalDate())) {
            throw new IllegalArgumentException(
                    "Event end date must match the date in event end time");
        }

        if (registrationDeadline != null
                && !registrationDeadline.isBefore(startTime)) {

            throw new IllegalArgumentException(
                    "Registration deadline must be before event start time");
        }

        if (eventMode == EventMode.ONLINE
                && (meetingUrl == null || meetingUrl.isBlank())) {

            throw new IllegalArgumentException(
                    "Meeting URL is required for online events");
        }

        if (eventMode == EventMode.ONSITE
                && (venue == null || venue.isBlank())) {

            throw new IllegalArgumentException(
                    "Venue is required for onsite events");
        }
    }

    // =========================================================
    // CAPACITY UPDATE VALIDATION
    // =========================================================

    private void validateCapacityUpdate(
            Event event,
            Integer newCapacity) {

        if (newCapacity == null) {
            return;
        }

        long registeredCount = registrationRepository.countByEventIdAndStatus(
                event.getId(),
                EventRegistrationStatus.REGISTERED
        );

        if (newCapacity < registeredCount) {
            throw new IllegalArgumentException(
                    "Capacity cannot be less than the current number of registrations: "
                            + registeredCount
            );
        }
    }

    // =========================================================
    // PUBLISH VALIDATION
    // =========================================================

    private void validatePublishableEvent(Event event) {

        if (event.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Past events cannot be published");
        }

        if (event.getRegistrationDeadline() != null
                && !event.getRegistrationDeadline().isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Registration deadline has expired");
        }
    }

    // =========================================================
    // MEETING URL VALIDATION
    // =========================================================

    private String validateMeetingUrl(String meetingUrl) {

        if (meetingUrl == null || meetingUrl.isBlank()) {
            return null;
        }

        try {

            URI uri = URI.create(meetingUrl.trim());

            String scheme = uri.getScheme();

            if (scheme == null
                    || !(scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https"))
                    || uri.getHost() == null) {

                throw new IllegalArgumentException();
            }

            return uri.toString();

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Meeting URL must be a valid HTTP or HTTPS URL");
        }
    }

    // =========================================================
    // MAP EVENT TO RESPONSE
    // =========================================================

    private EventResponse mapToResponse(Event event) {

        Institution institution = event.getInstitution();
        User creator = event.getCreatedBy();

        long registeredCount = registrationRepository.countByEventIdAndStatus(
                event.getId(),
                EventRegistrationStatus.REGISTERED
        );

        Integer capacity = event.getCapacity();

        Long availableSeats = capacity == null
                ? null
                : Math.max(0L, capacity.longValue() - registeredCount);

        LocalDateTime now = LocalDateTime.now();

        boolean registrationOpen =
                event.getStatus() == EventStatus.PUBLISHED
                && !event.getStartTime().isBefore(now)
                && (event.getRegistrationDeadline() == null
                    || event.getRegistrationDeadline().isAfter(now))
                && (capacity == null || registeredCount < capacity);

        return new EventResponse(
                event.getId(),
                institution.getId(),
                event.getTitle(),
                event.getEventType(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getEventMode(),
                event.getVenue(),
                event.getMeetingUrl(),
                capacity,
                event.getRegistrationDeadline(),
                event.getStatus(),

                registeredCount,
                availableSeats,
                registrationOpen,

                creator.getId(),
                creator.getFullName(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}

