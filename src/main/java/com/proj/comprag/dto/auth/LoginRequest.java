package com.proj.comprag.dto.auth;

public record LoginRequest(
        String email,
        String password) {

}