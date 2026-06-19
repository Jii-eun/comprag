package com.proj.comprag.common.exception;

import com.proj.comprag.common.exception.custom.BadRequestException;
import com.proj.comprag.common.exception.custom.ForbiddenException;
import com.proj.comprag.common.exception.custom.NotFoundException;
import com.proj.comprag.common.exception.custom.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    // 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest req) {
        return build(e.getErrorCode(), e.getMessage(), req);
    }

    // Spring 기본 예외를 apiErrorResponse로 처리
    // 400 - @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException e, HttpServletRequest req) {
        String msg = e.getBindingResult().getFieldErrors().isEmpty()
                ? ErrorCode.VALIDATION_ERROR.getDefaultMessage()
                : e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return build(ErrorCode.VALIDATION_ERROR, msg, req);
    }

    // 400
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingPathVar(MissingPathVariableException e, HttpServletRequest req) {
        return build(ErrorCode.BAD_REQUEST, "요청 경로 변수(path variable)가 누락되었습니다.", req);
    }

    // 401 - 문서없음
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
                                             UnauthorizedException e,
                                            HttpServletRequest req) {
        return build(e.getErrorCode(), e.getMessage(), req);
    }

    // 404 - 문서없음
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            NotFoundException e,
            HttpServletRequest req) {
        return build(e.getErrorCode(), e.getMessage(), req);
    }

    // 403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenException e, HttpServletRequest req) {
        return build(e.getErrorCode(), e.getMessage(), req);
    }


    // 나머지 전부 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception e, HttpServletRequest req) {

        // 운영에서는 로그 꼭 남겨 (여기서 e 찍기)
        e.printStackTrace(); // 혹은 log.error("Unhandled Exception: ", e);

        return build(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getDefaultMessage(), req);
    }

//    @ExceptionHandler()
//    public ResponseEntity<ApiErrorResponse> handleUnauthorized(Exception e, HttpServletRequest req) {
//        return build()
//    }



    private ResponseEntity<ApiErrorResponse> build(ErrorCode errorCode, String message, HttpServletRequest req) {
        int status = errorCode.getStatus().value();
        ApiErrorResponse body = ApiErrorResponse.of(
                errorCode,
                message,
                status,
                req.getRequestURI(),
                req.getMethod()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }
}
