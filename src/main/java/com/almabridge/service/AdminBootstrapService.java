
package com.almabridge.service;

import com.almabridge.entity.Institution;
import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;
import com.almabridge.repository.InstitutionRepository;
import com.almabridge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBootstrapService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createInitialAdmin(
            String fullName,
            String email,
            String password,
            Long institutionId) {

        // Normalize email
        String normalizedEmail = email.trim().toLowerCase();

        // Check whether the email already exists
        var existingUser = userRepository.findByEmail(normalizedEmail);

        if (existingUser.isPresent()) {

            User user = existingUser.get();

            // Never silently promote an existing account
            if (user.getRole() != UserRole.ADMIN) {
                throw new IllegalStateException(
                        "The configured admin email already belongs to a non-admin user"
                );
            }

            // Verify the existing admin belongs to the configured institution
            if (!user.getInstitution().getId().equals(institutionId)) {
                throw new IllegalStateException(
                        "Existing admin belongs to a different institution"
                );
            }

            System.out.println(
                    "AlmaBridge bootstrap: Admin already exists. Skipping creation."
            );

            return;
        }

        // Find the institution
        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() -> new IllegalStateException(
                        "Configured institution does not exist: " + institutionId
                ));

        // Create admin user
        User admin = new User();

        admin.setFullName(fullName.trim());
        admin.setEmail(normalizedEmail);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(UserRole.ADMIN);
        admin.setInstitution(institution);
        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println(
                "AlmaBridge bootstrap: Initial admin created successfully."
        );
    }
}
