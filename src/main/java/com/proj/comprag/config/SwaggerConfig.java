package com.proj.comprag.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("comprag 프로젝트 API 명세서")
                        .description("JavaSpring Boot 기반 API 서버 문서입니다.")
                        .version("1.0.0"));
    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("v1-definition")
//                .pathsToMatch("/api/**") // 실제 컨트롤러 주소가 /api로 시작한다면 이렇게 제한
//                .build();
//    }

}
