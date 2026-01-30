package com.proj.compRAG.web.auth;

import com.proj.compRAG.dto.auth.AuthResponse;
import com.proj.compRAG.dto.auth.LoginRequest;
import com.proj.compRAG.dto.auth.SignUpRequest;
import com.proj.compRAG.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 의존성 주입때문에 필요
    // AuthService가 또 다른 의존성을 갖게 되면 연쇄적으로 new해야함
    // 스프링에게 서비스를 주입해달라고 하는 방식 = 생성자 주입
    // 스프링이 실행 시점에 AuthService 빈을 찾앗 넣어줌
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request,
                                       HttpServletRequest httpReq) {
        System.out.println("METHOD=" + httpReq.getMethod() +"/ URI=" + httpReq.getRequestURI());
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }
    /** ResponseEntity
     - ResponseEntity는 Spring Web에 포함되어있음.
     - <T>는 응답 바디를 담을 타입
     -> 성공/실패만 알려도되고, 전달할 데이터가 없기 때문에 Body를 비운 것
     - ok() = HTTP 200 상태코드
     - buid() = 바디 없이 응답 객체 완성
     */

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
