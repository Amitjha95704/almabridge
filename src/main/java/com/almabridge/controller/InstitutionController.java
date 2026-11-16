package com.almabridge.controller;

import com.almabridge.dto.institution.InstitutionRequest;
import com.almabridge.dto.institution.InstitutionResponse;

import com.almabridge.service.institution.InstitutionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InstitutionResponse> createInstitution(
            @Valid @RequestBody InstitutionRequest request) {

        InstitutionResponse response =
                institutionService.createInstitution(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionResponse> getInstitutionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                institutionService.getInstitutionById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<InstitutionResponse>> getAllInstitutions() {

        return ResponseEntity.ok(
                institutionService.getAllInstitutions()
        );
    }
}