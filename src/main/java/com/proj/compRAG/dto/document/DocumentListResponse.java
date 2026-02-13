package com.proj.compRAG.dto.document;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentListResponse(
        UUID id,
        String title,
        UUID categoryId,
        String categoryName,
        UUID createdBy,
        String createdByName,
        OffsetDateTime updatedAt
) {
}
