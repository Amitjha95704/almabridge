package com.almabridge.dto.admin;

import jakarta.validation.constraints.NotNull;

public record StudentStatusRequest(

        @NotNull(message = "Enabled status is required")
        Boolean enabled

) {}