package com.proj.comprag.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /* WebConfig 용도
        스프링 MVC(웹 계층)의 동작을 커스터마이징할 때 사용.
        스프링 부트가 기본적으로 제공하는 웹 설정이 있지만,
        추가로 규칙을 넣어야할 때 선언하는 파일.
     */


    // 1. CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")    //모든 헤더 적용
                .allowCredentials(true);
    }
}