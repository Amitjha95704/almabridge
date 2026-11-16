package com.almabridge.dto.admin;

public record AdminStudentResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        Boolean enabled,
        String rollNumber,
        String branch,
        Integer graduationYear,
        String targetCareerDomain,
        String phoneNumber
) {}