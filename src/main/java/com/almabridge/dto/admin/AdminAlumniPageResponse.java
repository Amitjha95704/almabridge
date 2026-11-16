
package com.almabridge.dto.admin;

import java.util.List;

public record AdminAlumniPageResponse(
        List<AdminAlumniResponse> alumni,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {}
