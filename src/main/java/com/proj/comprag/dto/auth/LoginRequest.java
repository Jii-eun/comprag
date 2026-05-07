package com.proj.compRAG.dto.auth;

public record LoginRequest(
        String email,
        String password) {

}