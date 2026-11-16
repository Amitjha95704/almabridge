
package com.almabridge.service.admin.impl;

import com.almabridge.dto.admin.AdminInstitutionProfileResponse;
import com.almabridge.dto.admin.InstitutionProfileUpdateRequest;
import com.almabridge.entity.Institution;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.InstitutionRepository;
import com.almabridge.service.CurrentUserService;
//import com.almabridge.security.CurrentUserService;
import com.almabridge.service.admin.AdminInstitutionProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInstitutionProfileServiceImpl
        implements AdminInstitutionProfileService {

    private final InstitutionRepository institutionRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdminInstitutionProfileResponse getMyInstitution() {

        Long institutionId =
                currentUserService.getInstitutionId();

        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Institution not found"
                        )
                );

        return mapToResponse(institution);
    }

    @Override
    @Transactional
    public AdminInstitutionProfileResponse updateMyInstitution(
            InstitutionProfileUpdateRequest request) {

        Long institutionId =
                currentUserService.getInstitutionId();

        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Institution not found"
                        )
                );

        institution.setInstitutionName(
                request.institutionName().trim()
        );

        institution.setCity(
                request.city() == null || request.city().isBlank()
                        ? null
                        : request.city().trim()
        );

        institution.setState(
                request.state() == null || request.state().isBlank()
                        ? null
                        : request.state().trim()
        );

        Institution updatedInstitution =
                institutionRepository.save(institution);

        return mapToResponse(updatedInstitution);
    }

    private AdminInstitutionProfileResponse mapToResponse(
            Institution institution) {

        return new AdminInstitutionProfileResponse(
                institution.getId(),
                institution.getInstitutionName(),
                institution.getInstitutionCode(),
                institution.getCity(),
                institution.getState()
        );
    }
}
