package com.proj.comprag.dto.category;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryListResponse(
        UUID id,
        String name,
        String description,
        OffsetDateTime createdAt
) {
}
