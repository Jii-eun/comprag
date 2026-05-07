package com.proj.comprag.web.debug;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthControlller {

    private final BuildProperties buildProperties;

    public HealthControlller(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("version", buildProperties.getVersion()); // 빌드 시점의 버전
        response.put("buildTime", buildProperties.getTime().toString()); // 빌드된 시간
        return ResponseEntity.ok(response);
    }

}
