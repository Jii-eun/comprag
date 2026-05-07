package com.proj.compRAG.dto.auth;

public record SignUpRequest(
        String email,
        String password,
        String name) {

}
