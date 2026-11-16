package com.almabridge.dto.admin.analytics;

public record BranchDistributionResponse(
        String branch,
        Long total
) {}