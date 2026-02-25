package com.proj.comprag.web.debug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthControlller {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

}
