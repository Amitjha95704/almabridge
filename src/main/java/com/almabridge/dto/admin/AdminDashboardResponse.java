package com.almabridge.dto.admin;

public record AdminDashboardResponse(
        Long institutionId,
        long totalStudents,
        long totalAlumni,
        long activeStudents,
        long activeAlumni,
        long inactiveUsers,
        long totalUsers
) {}