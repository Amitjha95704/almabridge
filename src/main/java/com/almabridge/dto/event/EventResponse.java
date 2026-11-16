//
//package com.almabridge.dto.event;
//
//import com.almabridge.enums.EventMode;
//import com.almabridge.enums.EventStatus;
//import com.almabridge.enums.EventType;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//public record EventResponse(
//
//        Long id,
//
//        Long institutionId,
//
//        String title,
//
//        EventType eventType,
//
//        String description,
//
//        LocalDate startDate,
//
//        LocalDate endDate,
//
//        LocalDateTime startTime,
//
//        LocalDateTime endTime,
//
//        EventMode eventMode,
//
//        String venue,
//
//        String meetingUrl,
//
//        Integer capacity,
//
//        LocalDateTime registrationDeadline,
//
//        EventStatus status,
//
//        Long createdById,
//
//        String createdByName,
//
//        LocalDateTime createdAt,
//
//        LocalDateTime updatedAt
//
//) {
//}


package com.almabridge.dto.event;

import com.almabridge.enums.EventMode;
import com.almabridge.enums.EventStatus;
import com.almabridge.enums.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EventResponse(

        Long id,
        Long institutionId,
        String title,
        EventType eventType,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        EventMode eventMode,
        String venue,
        String meetingUrl,
        Integer capacity,
        LocalDateTime registrationDeadline,
        EventStatus status,

        Long registeredCount,
        Long availableSeats,
        boolean registrationOpen,

        Long createdById,
        String createdByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}