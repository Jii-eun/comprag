package com.proj.comprag.dto.auth;

import java.util.UUID;

public record UserPrincipal(
        UUID userId,
        String email,
        boolean isAdmin) {
}
