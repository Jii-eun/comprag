package com.proj.comprag.dto.document;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentCreateResponse(
        UUID id,
        String title,
        String content,
        UUID created_by,
        OffsetDateTime created_at) {
}
