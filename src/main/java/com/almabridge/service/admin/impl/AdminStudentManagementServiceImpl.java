package com.almabridge.service.admin.impl;

import com.almabridge.dto.admin.AdminStudentPageResponse;
import com.almabridge.dto.admin.AdminStudentResponse;
import com.almabridge.entity.StudentProfile;
import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;
//import com.almabridge.entity.UserRole;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.StudentProfileRepository;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
//import com.almabridge.security.CurrentUserService;
import com.almabridge.service.admin.AdminStudentManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStudentManagementServiceImpl
        implements AdminStudentManagementService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdminStudentPageResponse getStudents(
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

        Page<StudentProfile> studentPage =
                studentProfileRepository.searchStudents(
                        institutionId,
                        UserRole.STUDENT,
                        keyword,
                        pageable
                );

        return new AdminStudentPageResponse(
                studentPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList(),
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages(),
                studentPage.isLast()
        );
    }

    @Override
    public AdminStudentResponse getStudentById(Long studentId) {

        Long institutionId = currentUserService.getInstitutionId();

        StudentProfile profile =
                studentProfileRepository
                        .findStudentByUserAndInstitution(
                                studentId,
                                institutionId,
                                UserRole.STUDENT
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found in your institution"
                                )
                        );

        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public AdminStudentResponse updateStudentStatus(
            Long studentId,
            Boolean enabled) {

        if (enabled == null) {
            throw new IllegalArgumentException(
                    "Enabled status is required"
            );
        }

        Long institutionId = currentUserService.getInstitutionId();

        StudentProfile profile =
                studentProfileRepository
                        .findStudentByUserAndInstitution(
                                studentId,
                                institutionId,
                                UserRole.STUDENT
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found in your institution"
                                )
                        );

        User student = profile.getUser();
        student.setEnabled(enabled);

        User savedUser = userRepository.save(student);

        // Update the managed profile's associated user for response mapping
        profile.setUser(savedUser);

        return mapToResponse(profile);
    }

    private AdminStudentResponse mapToResponse(
            StudentProfile profile) {

        User user = profile.getUser();

        return new AdminStudentResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getEnabled(),
                profile.getRollNumber(),
                profile.getBranch(),
                profile.getGraduationYear(),
                profile.getTargetCareerDomain(),
                profile.getPhoneNumber()
        );
    }
}