
package com.almabridge.service.admin;

import com.almabridge.dto.admin.AdminAlumniPageResponse;
import com.almabridge.dto.admin.AdminAlumniResponse;

public interface AdminAlumniManagementService {

    AdminAlumniPageResponse getAlumni(
            String keyword,
            int page,
            int size
    );

    AdminAlumniResponse getAlumniById(Long alumniId);

    AdminAlumniResponse updateAlumniStatus(
            Long alumniId,
            Boolean enabled
    );
}
