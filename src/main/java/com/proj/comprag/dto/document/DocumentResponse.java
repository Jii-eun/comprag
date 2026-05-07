package com.proj.comprag.dto.document;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String title,
        UUID categoryId,
        String categoryName,
        String content,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        UUID createBy,
        String createdByName,
        UUID updatedBy,
        String updatedByName,     // 이후에 작성자 - 이름(팀명)으로 변경
        String editReason
) {
}
