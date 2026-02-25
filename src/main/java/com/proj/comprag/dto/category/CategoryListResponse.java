package com.proj.comprag.dto.document;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryListResponse(
        UUID id,
        String name,
        String description,
        OffsetDateTime createdAt
) {
}
