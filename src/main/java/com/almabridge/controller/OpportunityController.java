
package com.almabridge.controller;

import com.almabridge.dto.opportunity.CreateOpportunityRequest;
import com.almabridge.dto.opportunity.OpportunityResponse;
import com.almabridge.dto.opportunity.UpdateOpportunityRequest;
import com.almabridge.enums.OpportunityStatus;
import com.almabridge.enums.OpportunityType;
import com.almabridge.service.OpportunityService;

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
public class OpportunityController {

    private final OpportunityService opportunityService;

    // =====================================================
    // COLLEGE ADMIN APIs
    // =====================================================

    // Create a new opportunity
    @PostMapping("/admin/opportunities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpportunityResponse> createOpportunity(
            @Valid @RequestBody CreateOpportunityRequest request) {

        OpportunityResponse response =
                opportunityService.createOpportunity(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Update an existing opportunity
    @PutMapping("/admin/opportunities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpportunityResponse> updateOpportunity(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOpportunityRequest request) {

        OpportunityResponse response =
                opportunityService.updateOpportunity(id, request);

        return ResponseEntity.ok(response);
    }

    // Delete an opportunity
    @DeleteMapping("/admin/opportunities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOpportunity(
            @PathVariable Long id) {

        opportunityService.deleteOpportunity(id);

        return ResponseEntity.noContent().build();
    }

    // Publish an opportunity
    @PatchMapping("/admin/opportunities/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpportunityResponse> publishOpportunity(
            @PathVariable Long id) {

        OpportunityResponse response =
                opportunityService.publishOpportunity(id);

        return ResponseEntity.ok(response);
    }

    // Close an opportunity
    @PatchMapping("/admin/opportunities/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpportunityResponse> closeOpportunity(
            @PathVariable Long id) {

        OpportunityResponse response =
                opportunityService.closeOpportunity(id);

        return ResponseEntity.ok(response);
    }

    // Get all opportunities for the logged-in institution
    // Optional filter: DRAFT, PUBLISHED, CLOSED
    @GetMapping("/admin/opportunities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OpportunityResponse>> getAdminOpportunities(
            @RequestParam(required = false) OpportunityStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<OpportunityResponse> response =
                opportunityService.getAdminOpportunities(
                        status,
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // STUDENT APIs
    // =====================================================

    // View published opportunities from the student's institution
    // Optional filter: JOB, INTERNSHIP
    @GetMapping("/student/opportunities")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<OpportunityResponse>> getStudentOpportunities(
            @RequestParam(required = false) OpportunityType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<OpportunityResponse> response =
                opportunityService.getStudentOpportunities(
                        type,
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // View details of a published opportunity
    @GetMapping("/student/opportunities/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OpportunityResponse> getStudentOpportunityById(
            @PathVariable Long id) {

        OpportunityResponse response =
                opportunityService.getStudentOpportunityById(id);

        return ResponseEntity.ok(response);
    }
}
