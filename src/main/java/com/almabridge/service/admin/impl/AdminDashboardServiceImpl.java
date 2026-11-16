package com.almabridge.service.admin.impl;

import com.almabridge.dto.admin.AdminDashboardResponse;
import com.almabridge.enums.UserRole;
//import com.almabridge.entity.UserRole;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
//import com.almabridge.security.CurrentUserService;
import com.almabridge.service.admin.AdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdminDashboardResponse getDashboardOverview() {

        // Get the institution of the logged-in admin
        Long institutionId =
                currentUserService.getInstitutionId();

        // Total students
        long totalStudents =
                userRepository.countByInstitution_IdAndRole(
                        institutionId,
                        UserRole.STUDENT
                );

        // Total alumni
        long totalAlumni =
                userRepository.countByInstitution_IdAndRole(
                        institutionId,
                        UserRole.ALUMNI
                );

        // Active students
        long activeStudents =
                userRepository.countByInstitution_IdAndRoleAndEnabled(
                        institutionId,
                        UserRole.STUDENT,
                        true
                );

        // Active alumni
        long activeAlumni =
                userRepository.countByInstitution_IdAndRoleAndEnabled(
                        institutionId,
                        UserRole.ALUMNI,
                        true
                );

        // All inactive users in this institution
        long inactiveUsers =
                userRepository.countByInstitution_IdAndEnabled(
                        institutionId,
                        false
                );

        long totalUsers = totalStudents + totalAlumni;

        return new AdminDashboardResponse(
                institutionId,
                totalStudents,
                totalAlumni,
                activeStudents,
                activeAlumni,
                inactiveUsers,
                totalUsers
        );
    }
}