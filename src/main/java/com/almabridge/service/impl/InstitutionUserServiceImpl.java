
package com.almabridge.service.impl;

import com.almabridge.dto.auth.UserProfileResponse;
import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
import com.almabridge.service.InstitutionUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstitutionUserServiceImpl
        implements InstitutionUserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<UserProfileResponse> getStudentsOfCurrentInstitution() {

        if (currentUserService.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException(
                    "Only administrators can view student records"
            );
        }

        Long institutionId = currentUserService.getInstitutionId();

        return userRepository
                .findAllByInstitution_IdAndRole(
                        institutionId,
                        UserRole.STUDENT
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserProfileResponse getStudentById(Long studentId) {

        if (currentUserService.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException(
                    "Only administrators can view student records"
            );
        }

        Long institutionId = currentUserService.getInstitutionId();

        User student = userRepository
                .findByIdAndInstitution_Id(
                        studentId,
                        institutionId
                )
                .filter(user -> user.getRole() == UserRole.STUDENT)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found"
                        )
                );

        return mapToResponse(student);
    }

    private UserProfileResponse mapToResponse(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getInstitution().getId()
        );
    }
}
