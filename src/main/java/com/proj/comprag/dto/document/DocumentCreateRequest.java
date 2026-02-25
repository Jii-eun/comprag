package com.proj.comprag.dto.document;

public record DocumentCreateRequest(
        String title,
        String content,
        String editReason) {
}
