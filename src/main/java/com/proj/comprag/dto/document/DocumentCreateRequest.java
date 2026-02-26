package com.proj.comprag.dto.document;

import java.util.UUID;

public record DocumentCreateRequest(
        String title,
        String content,
        String editReason,
        UUID categoryId) {
}
