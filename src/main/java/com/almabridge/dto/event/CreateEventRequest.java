
package com.almabridge.dto.event;

import com.almabridge.enums.EventMode;
import com.almabridge.enums.EventType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateEventRequest(

        @NotBlank(message = "Event title is required")
        @Size(max = 200, message = "Title cannot exceed 200 characters")
        String title,

        @NotNull(message = "Event type is required")
        EventType eventType,

        @Size(max = 5000, message = "Description cannot exceed 5000 characters")
        String description,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date cannot be in the past")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @FutureOrPresent(message = "End date cannot be in the past")
        LocalDate endDate,

        @NotNull(message = "Start time is required")
        @FutureOrPresent(message = "Start time cannot be in the past")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime,

        @NotNull(message = "Event mode is required")
        EventMode eventMode,

        @Size(max = 200, message = "Venue cannot exceed 200 characters")
        String venue,

        @Size(max = 500, message = "Meeting URL cannot exceed 500 characters")
        String meetingUrl,

        @Positive(message = "Capacity must be greater than zero")
        Integer capacity,

        @Future(message = "Registration deadline must be in the future")
        LocalDateTime registrationDeadline

) {
}
