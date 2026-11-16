package com.almabridge.dto.admin.analytics;

public record GraduationYearDistributionResponse(
        Integer graduationYear,
        Long total
) {}