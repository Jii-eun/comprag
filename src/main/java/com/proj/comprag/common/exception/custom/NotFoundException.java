package com.proj.comprag.common.exception.custom;

import com.proj.comprag.common.exception.ErrorCode;

public class NotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() { return  errorCode;}
}
