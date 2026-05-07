package com.proj.comprag.dto.document;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record DocumentCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        String content,
        String editReason,
        UUID categoryId) {
}
