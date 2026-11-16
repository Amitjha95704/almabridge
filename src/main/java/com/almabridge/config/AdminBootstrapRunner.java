
package com.almabridge.config;

import com.almabridge.service.AdminBootstrapService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "almabridge.bootstrap-admin",
        name = "enabled",
        havingValue = "true"
)
public class AdminBootstrapRunner implements ApplicationRunner {

    private final AdminBootstrapService adminBootstrapService;

    @Value("${almabridge.bootstrap-admin.full-name}")
    private String fullName;

    @Value("${almabridge.bootstrap-admin.email}")
    private String email;

    @Value("${almabridge.bootstrap-admin.password}")
    private String password;

    @Value("${almabridge.bootstrap-admin.institution-id}")
    private Long institutionId;

    @Override
    public void run(ApplicationArguments args) {

        // Validate configuration
        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()
                || institutionId == null || institutionId <= 0) {

            throw new IllegalStateException(
                    "Invalid AlmaBridge admin bootstrap configuration"
            );
        }

        if (password.length() < 12 || password.length() > 72) {
            throw new IllegalStateException(
                    "Bootstrap admin password must contain 12 to 72 characters"
            );
        }

        adminBootstrapService.createInitialAdmin(
                fullName,
                email,
                password,
                institutionId
        );
    }
}
