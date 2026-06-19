package com.proj.comprag.web.rag;

import com.proj.comprag.service.rag.RagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    private RagController(RagService ragService) {
        this.ragService = ragService;
    }

    //구조 정리다시

}
