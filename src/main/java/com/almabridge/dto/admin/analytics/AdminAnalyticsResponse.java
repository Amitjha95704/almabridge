package com.almabridge.dto.admin.analytics;

import java.util.List;

public record AdminAnalyticsResponse(

        Long institutionId,

        List<BranchDistributionResponse> studentsByBranch,

        List<GraduationYearDistributionResponse>
                studentsByGraduationYear,

        List<AlumniCompanyDistributionResponse> alumniByCompany,

        List<AccountStatusDistributionResponse> accountStatus

) {}