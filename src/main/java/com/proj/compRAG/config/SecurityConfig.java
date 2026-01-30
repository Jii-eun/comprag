package com.proj.compRAG.config;

import com.proj.compRAG.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // 우선, 로그인없이 사용할 수 있도록 인증 우회 처리
//    private final JwtAuthFilter jwtAuthFilter;
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .formLogin(form -> form.disable())
//                .httpBasic(basic -> basic.disable());
//
//        return http.build();
//    }

    // jwt 토큰 추가 후 재작성
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    JwtAuthFilter jwtAuthFilter)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm
                        -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   // stateless 설정
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers("/health", "/api/auth/**", "/h2-console/**")    //permit all
                        .permitAll().anyRequest().authenticated())  // anyRequest().authenticated()) 이 처리로 인해 기본적으로 전부 로그인 필요 상태로 처리됨
                .headers(headers
                        -> headers.frameOptions(frame -> frame.disable())) //h2-console용
                .formLogin(form -> form.disable())     // API 서버면 보통 끔
                .httpBasic(basic -> basic.disable())    // API 서버면 보통 끔
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);    //JwtAuthFilter를 메서드 파라미터로 주입

        // 인가 정책은 /auth/** 같은 엔드포인트만 permitAll로 열고 나머지는 authenticated로 잠금

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 모르겟어... https://chanho0912.tistory.com/33
    }
}

