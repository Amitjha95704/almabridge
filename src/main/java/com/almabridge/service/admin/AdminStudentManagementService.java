package com.almabridge.service.admin;

import com.almabridge.dto.admin.AdminStudentPageResponse;
import com.almabridge.dto.admin.AdminStudentResponse;

public interface AdminStudentManagementService {

    AdminStudentPageResponse getStudents(
            String keyword,
            int page,
            int size
    );

    AdminStudentResponse getStudentById(Long studentId);

    AdminStudentResponse updateStudentStatus(
            Long studentId,
            Boolean enabled
    );
}