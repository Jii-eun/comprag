package com.proj.compRAG.dto.document;

import java.util.UUID;

public record DocumentUpdateResponse(
        UUID documentId,
        UUID latestVersionId,
        boolean changed ) {
}
