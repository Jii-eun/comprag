package com.proj.comprag.dto.auth;

public record SignUpRequest(
        String email,
        String password,
        String name) {

}
