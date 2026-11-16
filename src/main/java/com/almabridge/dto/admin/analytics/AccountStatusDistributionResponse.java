package com.almabridge.dto.admin.analytics;

public record AccountStatusDistributionResponse(
        Boolean enabled,
        Long total
) {}