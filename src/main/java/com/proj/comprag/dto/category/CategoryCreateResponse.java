package com.proj.comprag.dto.category;

import java.util.UUID;

public record CategoryCreateResponse(
        UUID id,
        String name
) {
}
