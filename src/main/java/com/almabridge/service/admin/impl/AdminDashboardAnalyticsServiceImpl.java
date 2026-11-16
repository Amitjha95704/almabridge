package com.almabridge.service.admin.impl;

import com.almabridge.dto.admin.analytics.AdminAnalyticsResponse;
import com.almabridge.enums.UserRole;
//import com.almabridge.entity.UserRole;
import com.almabridge.repository.AlumniProfileRepository;
import com.almabridge.repository.StudentProfileRepository;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
//import com.almabridge.security.CurrentUserService;
import com.almabridge.service.admin.AdminDashboardAnalyticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardAnalyticsServiceImpl
        implements AdminDashboardAnalyticsService {

    private final StudentProfileRepository studentProfileRepository;

    private final AlumniProfileRepository alumniProfileRepository;

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;

    @Override
    public AdminAnalyticsResponse getAnalytics() {

        // Get institution ID from authenticated admin
        Long institutionId =
                currentUserService.getInstitutionId();

        // Student distribution by branch
        var studentsByBranch =
                studentProfileRepository.getBranchDistribution(
                        institutionId,
                        UserRole.STUDENT
                );

        var studentsByGraduationYear =
                studentProfileRepository.getGraduationYearDistribution(
                        institutionId,
                        UserRole.STUDENT
                );

        // Alumni distribution by company
        var alumniByCompany =
                alumniProfileRepository.getCompanyDistribution(
                        institutionId,
                        UserRole.ALUMNI
                );

        // Active and inactive student/alumni accounts
        var accountStatus =
                userRepository.getAccountStatusDistribution(
                        institutionId,
                        List.of(
                                UserRole.STUDENT,
                                UserRole.ALUMNI
                        )
                );

        return new AdminAnalyticsResponse(
                institutionId,
                studentsByBranch,
                studentsByGraduationYear,
                alumniByCompany,
                accountStatus
        );
    }
}