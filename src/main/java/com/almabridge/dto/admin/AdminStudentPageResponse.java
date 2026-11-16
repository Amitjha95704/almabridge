
package com.almabridge.dto.admin;

import java.util.List;

public record AdminStudentPageResponse(
        List<AdminStudentResponse> students,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {}
