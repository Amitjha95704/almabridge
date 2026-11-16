
package com.almabridge.controller;

import com.almabridge.dto.admin.AdminAlumniPageResponse;
import com.almabridge.dto.admin.AdminAlumniResponse;
import com.almabridge.dto.admin.AlumniStatusRequest;
import com.almabridge.service.admin.AdminAlumniManagementService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/alumni")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAlumniController {

    private final AdminAlumniManagementService alumniService;

    // Get all alumni with pagination
    @GetMapping
    public ResponseEntity<AdminAlumniPageResponse> getAllAlumni(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                alumniService.getAlumni(keyword, page, size)
        );
    }

    // Search alumni
    @GetMapping("/search")
    public ResponseEntity<AdminAlumniPageResponse> searchAlumni(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                alumniService.getAlumni(keyword, page, size)
        );
    }

    // Get alumni details
    @GetMapping("/{alumniId}")
    public ResponseEntity<AdminAlumniResponse> getAlumniById(
            @PathVariable Long alumniId) {

        return ResponseEntity.ok(
                alumniService.getAlumniById(alumniId)
        );
    }

    // Activate or deactivate alumni
    @PatchMapping("/{alumniId}/status")
    public ResponseEntity<AdminAlumniResponse> updateAlumniStatus(
            @PathVariable Long alumniId,
            @Valid @RequestBody AlumniStatusRequest request) {

        return ResponseEntity.ok(
                alumniService.updateAlumniStatus(
                        alumniId,
                        request.enabled()
                )
        );
    }
}
