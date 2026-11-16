
package com.almabridge.dto.admin;

public record AdminAlumniResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        Boolean enabled,
        Integer graduationYear,
        String branch,
        String companyName,
        String designation,
        Integer experienceYears
) {}
