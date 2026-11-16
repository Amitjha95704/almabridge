//
//package com.almabridge.service.impl;
//
//import com.almabridge.dto.auth.*;
//import com.almabridge.entity.*;
//import com.almabridge.enums.UserRole;
//import com.almabridge.exception.DuplicateResourceException;
//import com.almabridge.exception.ResourceNotFoundException;
//import com.almabridge.repository.*;
//import com.almabridge.security.CustomUserDetails;
//import com.almabridge.service.AuthService;
//import com.almabridge.security.JwtService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final UserRepository userRepository;
//    private final InstitutionRepository institutionRepository;
//    private final StudentProfileRepository studentProfileRepository;
//    private final AlumniProfileRepository alumniProfileRepository;
//
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//
//    @Override
//    @Transactional
//    public RegistrationResponse registerStudent(
//            StudentRegistrationRequest request) {
//
//        validateEmail(request.email());
//
//        Institution institution = getActiveInstitution(
//                request.institutionId());
//
//        User user = new User();
//        user.setFullName(request.fullName().trim());
//        user.setEmail(request.email().trim().toLowerCase());
//        user.setPasswordHash(
//                passwordEncoder.encode(request.password()));
//        user.setRole(UserRole.STUDENT);
//        user.setInstitution(institution);
//        user.setEnabled(true);
//
//        User savedUser = userRepository.save(user);
//
//        StudentProfile profile = new StudentProfile();
//        profile.setUser(savedUser);
//        profile.setRollNumber(request.rollNumber());
//        profile.setBranch(request.branch());
//        profile.setGraduationYear(request.graduationYear());
//        profile.setTargetCareerDomain(request.targetCareerDomain());
//        profile.setPhoneNumber(request.phoneNumber());
//
//        studentProfileRepository.save(profile);
//
//        return new RegistrationResponse(
//                savedUser.getId(),
//                savedUser.getFullName(),
//                savedUser.getEmail(),
//                savedUser.getRole().name(),
//                institution.getId(),
//                "Student registered successfully"
//        );
//    }
//
//    @Override
//    @Transactional
//    public RegistrationResponse registerAlumni(
//            AlumniRegistrationRequest request) {
//
//        validateEmail(request.email());
//
//        Institution institution = getActiveInstitution(
//                request.institutionId());
//
//        User user = new User();
//        user.setFullName(request.fullName().trim());
//        user.setEmail(request.email().trim().toLowerCase());
//        user.setPasswordHash(
//                passwordEncoder.encode(request.password()));
//        user.setRole(UserRole.ALUMNI);
//        user.setInstitution(institution);
//        user.setEnabled(true);
//
//        User savedUser = userRepository.save(user);
//
//        AlumniProfile profile = new AlumniProfile();
//        profile.setUser(savedUser);
//        profile.setGraduationYear(request.graduationYear());
//        profile.setBranch(request.branch());
//        profile.setCompanyName(request.companyName());
//        profile.setDesignation(request.designation());
//        profile.setExperienceYears(request.experienceYears());
//
//        alumniProfileRepository.save(profile);
//
//        return new RegistrationResponse(
//                savedUser.getId(),
//                savedUser.getFullName(),
//                savedUser.getEmail(),
//                savedUser.getRole().name(),
//                institution.getId(),
//                "Alumni registered successfully"
//        );
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public AuthResponse login(String email, String password) {
//
//        Authentication authentication =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                email.trim().toLowerCase(),
//                                password
//                        )
//                );
//
//        CustomUserDetails userDetails =
//                (CustomUserDetails) authentication.getPrincipal();
//
//        User user = userRepository.findById(userDetails.getUserId())
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User not found"));
//
//        String token = jwtService.generateToken(userDetails);
//
//        return new AuthResponse(
//                token,
//                "Bearer",
//                user.getId(),
//                user.getFullName(),
//                user.getEmail(),
//                user.getRole().name(),
//                user.getInstitution().getId()
//        );
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserProfileResponse getCurrentUser(Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User not found"));
//
//        return new UserProfileResponse(
//                user.getId(),
//                user.getFullName(),
//                user.getEmail(),
//                user.getRole().name(),
//                user.getInstitution().getId()
//        );
//    }
//
//    private void validateEmail(String email) {
//
//        String normalizedEmail = email.trim().toLowerCase();
//
//        if (userRepository.existsByEmail(normalizedEmail)) {
//            throw new DuplicateResourceException(
//                    "An account with this email already exists");
//        }
//    }
//
//    private Institution getActiveInstitution(Long institutionId) {
//
//        Institution institution = institutionRepository
//                .findById(institutionId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Institution not found"));
//
//        if (!institution.getActive()) {
//            throw new IllegalArgumentException(
//                    "This institution is currently inactive");
//        }
//
//        return institution;
//    }
//}



package com.almabridge.service.impl;

import com.almabridge.dto.auth.*;
import com.almabridge.entity.*;
import com.almabridge.enums.UserRole;
import com.almabridge.exception.DuplicateResourceException;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.*;
import com.almabridge.security.CustomUserDetails;
import com.almabridge.security.JwtService;
import com.almabridge.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final AlumniProfileRepository alumniProfileRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RegistrationResponse registerStudent(
            StudentRegistrationRequest request) {

        validateEmail(request.email());

        Institution institution = getActiveInstitution(
                request.institutionId());

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(
                passwordEncoder.encode(request.password()));
        user.setRole(UserRole.STUDENT);
        user.setInstitution(institution);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setUser(savedUser);
        profile.setRollNumber(request.rollNumber());
        profile.setBranch(request.branch());
        profile.setGraduationYear(request.graduationYear());
        profile.setTargetCareerDomain(request.targetCareerDomain());
        profile.setPhoneNumber(request.phoneNumber());

        studentProfileRepository.save(profile);

        return new RegistrationResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                institution.getId(),
                "Student registered successfully"
        );
    }

    @Override
    @Transactional
    public RegistrationResponse registerAlumni(
            AlumniRegistrationRequest request) {

        validateEmail(request.email());

        Institution institution = getActiveInstitution(
                request.institutionId());

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(
                passwordEncoder.encode(request.password()));
        user.setRole(UserRole.ALUMNI);
        user.setInstitution(institution);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        AlumniProfile profile = new AlumniProfile();
        profile.setUser(savedUser);
        profile.setGraduationYear(request.graduationYear());
        profile.setBranch(request.branch());
        profile.setCompanyName(request.companyName());
        profile.setDesignation(request.designation());
        profile.setExperienceYears(request.experienceYears());

        alumniProfileRepository.save(profile);

        return new RegistrationResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                institution.getId(),
                "Alumni registered successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(String email, String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email.trim().toLowerCase(),
                                password
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(userDetails);

        // Super Admin does not belong to any institution.
        // College users have an associated institution.
        Long institutionId = user.getInstitution() != null
                ? user.getInstitution().getId()
                : null;

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                institutionId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Return null institutionId for Super Admin.
        Long institutionId = user.getInstitution() != null
                ? user.getInstitution().getId()
                : null;

        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                institutionId
        );
    }

    private void validateEmail(String email) {

        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "An account with this email already exists");
        }
    }

    private Institution getActiveInstitution(Long institutionId) {

        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Institution not found"));

        if (!institution.getActive()) {
            throw new IllegalArgumentException(
                    "This institution is currently inactive");
        }

        return institution;
    }
}
