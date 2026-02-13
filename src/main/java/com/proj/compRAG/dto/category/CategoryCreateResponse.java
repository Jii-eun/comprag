package com.proj.compRAG.dto.document;

import java.util.UUID;

public record CategoryCreateResponse(
        UUID id,
        String name
) {
}
