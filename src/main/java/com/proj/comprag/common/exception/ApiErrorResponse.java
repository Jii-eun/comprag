package com.proj.comprag.common.exception;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
        String code,
        String meessage,
        int status,
        String path,
        String method,
        OffsetDateTime time
) {
    public static ApiErrorResponse of(
            ErrorCode errorCode, String message, int status,
            String path, String method) {
        return new ApiErrorResponse(
                errorCode.getCode(),
                message,
                status,
                path,
                method,
                OffsetDateTime.now()
        );
    }
}
