package com.proj.comprag.dto.auth;

import java.util.UUID;

//AuthResponse 안에 넣을 "user"
public record UserSummaryResponse(
        UUID id,
        String email,
        String name,
        boolean isAdmin ) {

}