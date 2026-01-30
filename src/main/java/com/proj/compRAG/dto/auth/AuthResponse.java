package com.proj.compRAG.dto.auth;

public record AuthResponse(
        String accessToken,
        UserSummaryResponse user) {

}