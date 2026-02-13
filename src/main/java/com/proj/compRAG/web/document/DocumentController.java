package com.proj.compRAG.web.document;

import com.proj.compRAG.dto.document.DocRequest;
import com.proj.compRAG.service.document.DocService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doc")
public class DocController {

    private final DocService docService;

    public DocController(DocService docService) { this.docService = docService; }

    // 문서 작성
    public ResponseEntity<Void> createDoc(@RequestBody DocRequest request,
                                          HttpServletRequest httpReq)

        docService.createDoc(request);

        return ResponseEntity<Void>;

}

