package com.proj.compRAG.dto.document;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String title,
        String content,
        UUID created_by,
        OffsetDateTime created_at) {
}
