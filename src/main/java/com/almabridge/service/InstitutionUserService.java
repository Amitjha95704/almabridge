
package com.almabridge.service;

import com.almabridge.dto.auth.UserProfileResponse;

import java.util.List;

public interface InstitutionUserService {

    List<UserProfileResponse> getStudentsOfCurrentInstitution();

    UserProfileResponse getStudentById(Long studentId);
}
