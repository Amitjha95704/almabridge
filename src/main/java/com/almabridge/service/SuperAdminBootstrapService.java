
package com.almabridge.service;

import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;
import com.almabridge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuperAdminBootstrapService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createInitialSuperAdmin(
            String fullName,
            String email,
            String password) {

        String normalizedEmail =
                email.trim().toLowerCase();

        var existingUser =
                userRepository.findByEmail(normalizedEmail);

        if (existingUser.isPresent()) {

            User user = existingUser.get();

            if (user.getRole() != UserRole.SUPER_ADMIN) {
                throw new IllegalStateException(
                        "Configured Super Admin email already belongs to another role."
                );
            }

            if (user.getInstitution() != null) {
                throw new IllegalStateException(
                        "Super Admin must not belong to an institution."
                );
            }

            System.out.println(
                    "AlmaBridge: Super Admin already exists. Skipping creation."
            );

            return;
        }

        User superAdmin = new User();

        superAdmin.setFullName(fullName.trim());
        superAdmin.setEmail(normalizedEmail);
        superAdmin.setPasswordHash(
                passwordEncoder.encode(password)
        );
        superAdmin.setRole(UserRole.SUPER_ADMIN);

        // Super Admin is not associated with any institution
        superAdmin.setInstitution(null);

        superAdmin.setEnabled(true);

        userRepository.save(superAdmin);

        System.out.println(
                "AlmaBridge: Initial Super Admin created successfully."
        );
    }
}
