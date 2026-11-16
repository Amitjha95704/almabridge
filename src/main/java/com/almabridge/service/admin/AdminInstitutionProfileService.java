
package com.almabridge.service.admin;

import com.almabridge.dto.admin.AdminInstitutionProfileResponse;
import com.almabridge.dto.admin.InstitutionProfileUpdateRequest;

public interface AdminInstitutionProfileService {

    AdminInstitutionProfileResponse getMyInstitution();

    AdminInstitutionProfileResponse updateMyInstitution(
            InstitutionProfileUpdateRequest request
    );
}
