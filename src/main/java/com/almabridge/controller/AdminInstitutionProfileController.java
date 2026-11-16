
package com.almabridge.controller;

import com.almabridge.dto.admin.AdminInstitutionProfileResponse;
import com.almabridge.dto.admin.InstitutionProfileUpdateRequest;
import com.almabridge.service.admin.AdminInstitutionProfileService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/institution")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInstitutionProfileController {

    private final AdminInstitutionProfileService institutionService;

    // View logged-in admin's institution
    @GetMapping
    public ResponseEntity<AdminInstitutionProfileResponse>
            getMyInstitution() {

        return ResponseEntity.ok(
                institutionService.getMyInstitution()
        );
    }

    // Update logged-in admin's institution
    @PutMapping
    public ResponseEntity<AdminInstitutionProfileResponse>
            updateMyInstitution(
                    @Valid @RequestBody
                    InstitutionProfileUpdateRequest request) {

        return ResponseEntity.ok(
                institutionService.updateMyInstitution(request)
        );
    }
}
