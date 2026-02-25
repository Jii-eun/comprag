package com.proj.comprag.web.document;

import com.proj.comprag.dto.document.*;
import com.proj.comprag.service.document.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/docs")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // 문서 작성
    @PostMapping
    public ResponseEntity<UUID> createDocument(@RequestBody DocumentCreateRequest request,
                                            HttpServletRequest httpReq,
                                            Authentication authentication) {
        // principal = userId. toString으로 넣어둔 상태
        String principal = (String) authentication.getPrincipal();
        UUID userId = UUID.fromString(principal);
        UUID documentId = documentService.createDocument(userId, request);

        return ResponseEntity
                .created(URI.create("/api/docs/" + documentId))
                .body(documentId);
    }

    // 목록 조회
    @GetMapping
    public ResponseEntity<Page<DocumentListResponse>> selectDocuments(
            @PageableDefault(sort="updatedAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {

        Page<DocumentListResponse> responses = documentService.selectDocuments(pageable);

        return ResponseEntity.ok(responses);
    }

    // 상세 조회
    @GetMapping("/{docId}")
    public ResponseEntity<DocumentResponse> selectDocument(@PathVariable UUID docId,
                                                    @AuthenticationPrincipal String principal) {

        UUID userId = UUID.fromString(principal);
        DocumentResponse response = documentService.selectDocument(docId);

        return ResponseEntity.ok(response);
    }

    // 수정
    @PatchMapping("/{docId}")
    public ResponseEntity<DocumentUpdateResponse> updateDocument(@PathVariable UUID docId,
                                                    @RequestBody DocumentUpdateRequest request,
                                                    @AuthenticationPrincipal String principal) {

        UUID userId = UUID.fromString(principal);
        DocumentUpdateResponse response = documentService.updateDocument(userId, docId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{docId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID docId) {

        documentService.deleteDocument(docId);
        return ResponseEntity.noContent().build();
    }

}

