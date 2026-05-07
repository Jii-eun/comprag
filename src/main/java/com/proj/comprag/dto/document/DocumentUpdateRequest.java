package com.proj.comprag.dto.document;

import java.util.UUID;

public record DocumentUpdateRequest(
        UUID id,
        String title,
        String content,
        UUID categoryId,
        String editReason
        //수정시에는 문서버전 수정 후 문서 수정 순서로
        ) {
}
