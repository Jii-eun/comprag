package com.proj.comprag.dto.auth;

public record AuthResponse(
        String accessToken,
        UserSummaryResponse user) {

}