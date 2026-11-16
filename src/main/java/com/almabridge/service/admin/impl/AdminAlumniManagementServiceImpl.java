
package com.almabridge.service.admin.impl;

import com.almabridge.dto.admin.AdminAlumniPageResponse;
import com.almabridge.dto.admin.AdminAlumniResponse;
import com.almabridge.entity.AlumniProfile;
import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;
//import com.almabridge.entity.UserRole;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.AlumniProfileRepository;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
//import com.almabridge.security.CurrentUserService;
import com.almabridge.service.admin.AdminAlumniManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAlumniManagementServiceImpl
        implements AdminAlumniManagementService {

    private final AlumniProfileRepository alumniProfileRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdminAlumniPageResponse getAlumni(
            String keyword,
            int page,
            int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        Long institutionId = currentUserService.getInstitutionId();

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        Page<AlumniProfile> alumniPage =
                alumniProfileRepository.searchAlumni(
                        institutionId,
                        UserRole.ALUMNI,
                        keyword,
                        pageable
                );

        return new AdminAlumniPageResponse(
                alumniPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList(),
                alumniPage.getNumber(),
                alumniPage.getSize(),
                alumniPage.getTotalElements(),
                alumniPage.getTotalPages(),
                alumniPage.isLast()
        );
    }

    @Override
    public AdminAlumniResponse getAlumniById(Long alumniId) {

        Long institutionId = currentUserService.getInstitutionId();

        AlumniProfile profile =
                alumniProfileRepository
                        .findAlumniByUserAndInstitution(
                                alumniId,
                                institutionId,
                                UserRole.ALUMNI
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Alumni not found in your institution"
                                )
                        );

        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public AdminAlumniResponse updateAlumniStatus(
            Long alumniId,
            Boolean enabled) {

        if (enabled == null) {
            throw new IllegalArgumentException(
                    "Enabled status is required"
            );
        }

        Long institutionId = currentUserService.getInstitutionId();

        AlumniProfile profile =
                alumniProfileRepository
                        .findAlumniByUserAndInstitution(
                                alumniId,
                                institutionId,
                                UserRole.ALUMNI
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Alumni not found in your institution"
                                )
                        );

        User alumni = profile.getUser();
        alumni.setEnabled(enabled);

        User savedUser = userRepository.save(alumni);
        profile.setUser(savedUser);

        return mapToResponse(profile);
    }

    private AdminAlumniResponse mapToResponse(
            AlumniProfile profile) {

        User user = profile.getUser();

        return new AdminAlumniResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getEnabled(),
                profile.getGraduationYear(),
                profile.getBranch(),
                profile.getCompanyName(),
                profile.getDesignation(),
                profile.getExperienceYears()
        );
    }
}
