
package com.almabridge.entity;

import com.almabridge.enums.EventRegistrationStatus;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "event_registrations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_event_registration_user",
            columnNames = {"event_id", "user_id"}
        )
    },
    indexes = {
        @Index(
            name = "idx_event_registration_event",
            columnList = "event_id"
        ),
        @Index(
            name = "idx_event_registration_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_event_registration_status",
            columnList = "status"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Event for which the user is registering
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Student or Alumni who registered
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventRegistrationStatus status =
            EventRegistrationStatus.REGISTERED;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    private LocalDateTime cancelledAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        registeredAt = now;
        updatedAt = now;

        if (status == null) {
            status = EventRegistrationStatus.REGISTERED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
