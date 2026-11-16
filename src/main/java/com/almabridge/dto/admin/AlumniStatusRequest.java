
package com.almabridge.dto.admin;

import jakarta.validation.constraints.NotNull;

public record AlumniStatusRequest(

        @NotNull(message = "Enabled status is required")
        Boolean enabled

) {}
