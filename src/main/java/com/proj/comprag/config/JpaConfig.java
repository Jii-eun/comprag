package com.proj.comprag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaConfig {

    /*
        기본으로 localDateTime값이 들어가기 때문에,
        기존 설정값인 OffsetDateTime으로 변경하는 설정 추가
        (OffsetDateTime은 시간대 정보를 포함함)ㄴ
     */

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
