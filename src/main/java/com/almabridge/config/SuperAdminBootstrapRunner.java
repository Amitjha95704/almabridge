
package com.almabridge.config;

import com.almabridge.service.SuperAdminBootstrapService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "almabridge.bootstrap-super-admin",
        name = "enabled",
        havingValue = "true"
)
public class SuperAdminBootstrapRunner implements ApplicationRunner {

    private final SuperAdminBootstrapService superAdminBootstrapService;

    @Value("${almabridge.bootstrap-super-admin.full-name}")
    private String fullName;

    @Value("${almabridge.bootstrap-super-admin.email}")
    private String email;

    @Value("${almabridge.bootstrap-super-admin.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {

        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            throw new IllegalStateException(
                    "Invalid AlmaBridge Super Admin bootstrap configuration"
            );
        }

        if (password.length() < 12 || password.length() > 72) {
            throw new IllegalStateException(
                    "Super Admin password must contain 12 to 72 characters"
            );
        }

        superAdminBootstrapService.createInitialSuperAdmin(
                fullName,
                email,
                password
        );
    }
}
