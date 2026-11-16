
package com.almabridge.service;

import com.almabridge.dto.auth.AlumniRegistrationRequest;
import com.almabridge.dto.auth.AuthResponse;
import com.almabridge.dto.auth.RegistrationResponse;
import com.almabridge.dto.auth.StudentRegistrationRequest;
import com.almabridge.dto.auth.UserProfileResponse;

public interface AuthService {

    RegistrationResponse registerStudent(
            StudentRegistrationRequest request);

    RegistrationResponse registerAlumni(
            AlumniRegistrationRequest request);

    AuthResponse login(String email, String password);

    UserProfileResponse getCurrentUser(Long userId);
}
