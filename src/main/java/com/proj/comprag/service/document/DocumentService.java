package com.proj.comprag.service.document;

import com.proj.comprag.common.exception.ErrorCode;
import com.proj.comprag.common.exception.NotFoundException;
import com.proj.comprag.domain.category.repository.CategoryRepository;
import com.proj.comprag.domain.document.entity.Document;
import com.proj.comprag.domain.document.entity.DocumentVersion;
import com.proj.comprag.domain.document.repository.DocumentRepository;
import com.proj.comprag.domain.document.repository.DocumentVersionRepository;
import com.proj.comprag.domain.user.repository.UserRepository;
import com.proj.comprag.dto.document.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor    //이거뭔데;
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DocumentService(DocumentRepository documentRepository,
                           DocumentVersionRepository documentVersionRepository,
                           UserRepository userRepository, CategoryRepository categoryRepository) {
        this.documentRepository = documentRepository;
        this.documentVersionRepository = documentVersionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional  //jakarta, springframework 상관없는지
    public UUID createDocument(UUID userId, DocumentCreateRequest request) {


        // 1) 카테고리 ID 검증

        // 비즈니스 로직 추가(카테고리 아이디 체크) ★★★★★g해야함...
//        if (!categoryRepository.existsById(request.categoryId())) {
//            throw new CategoryNotFoundException(request.categoryId()); // 커스텀 예외
//        }
        UUID categoryId = request.categoryId();

        // 2) documents 테이블 저장 (title, created_by, created_at)
        Document doc = new Document(
                request.title().trim(),
                userId,
                categoryId
        );

        Document savedDoc = documentRepository.save(doc);

        // 3) document_versions 테이블 저장 (content, created_at, updated_at
        UUID docId = savedDoc.getId();
        UUID versionId = savedDoc.getLatestVersionId();

        DocumentVersion docVer = new DocumentVersion(
            versionId,
            docId,
            1,
            request.content(),
            request.editReason(),
            userId
        );

        documentVersionRepository.save(docVer);

        // 4) document latest_version_id 수정
        savedDoc.changeLatestVersionId(versionId);

        return docId;
    }

    // 목록 조회
    public Page<DocumentListResponse> selectDocuments(Pageable pageable) {

        // projection 방식 테이블 join해서 userName, categoryName을 한번에 조회
        return documentRepository.findDocsList(pageable);



    }

    // 상세 조회(w.content)
    public DocumentResponse selectDocument(UUID docId) {

        // 처음 버전
////        Document doc = documentRepository.findAllById(docId);     // ???
//        Document doc = loadDocument(docId);
//
////        Optional<DocumentVersion> optional = documentVersionRepository.findTopByDocumentIdOrderByVersionNumberDesc(docId);
////        DocumentVersion docVer = optional.orElseThrow(() -> new IllegalStateException("문서 버전이 존재하지 않습니다."));
//        DocumentVersion docVer = loadDocumentVersion(docId);
//
//        User user = userRepository.findOneById(userId);
//
//        DocumentResponse response = new DocumentResponse(
//                doc.getId(),
//                doc.getTitle(),
//                docVer.getContent(),
//                doc.getCreatedAt(),
//                docVer.getCreatedAt(),
//                docVer.getCreatedBy(),
//                user.getName()
//        );

        // 단순조회 projection JPQL + DTO  처리

        return documentRepository.findDocWithContById(docId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    @Transactional
    public DocumentUpdateResponse updateDocument(UUID userId, UUID docId, DocumentUpdateRequest request) {

        // 문서 검증 + 조회
        Document doc = loadDocument(docId);
        log.info("docId = " + docId);

        // 문서 버전 검증 + 최신 버전 조회
//        Optional<DocumentVersion> optional = documentVersionRepository.findTopByDocumentIdOrderByVersionNumberDesc(docId);
//        DocumentVersion latest = optional.orElseThrow(() -> new IllegalStateException("문서 버전이 존재하지 않습니다."));
        DocumentVersion docVer = loadDocumentVersion(docId);
        // @@@ 여기서 NULL넘어왔음 체크해야함

        boolean titleChanged = request.title() != null && !request.title().equals(doc.getTitle());
        boolean categoryChanged = request.categoryId() != null && !request.categoryId().equals(doc.getCategoryId());
        boolean contentChanged = request.content() != null && !request.content().equals(docVer.getContent());

        log.info("titleChanged = " + titleChanged);
        log.info("categoryChanged = " + categoryChanged);
        log.info("contentChanged = " + contentChanged);

        UUID nextVerId = UUID.randomUUID();

        if(titleChanged || categoryChanged || contentChanged) {
            // 수정한 내용이 있을 경우
            OffsetDateTime now = OffsetDateTime.now();
//            int nextVerNum = optional.map(v -> v.getVersionNumber() + 1).orElse(1);
            int nextVerNum = docVer.getVersionNumber() + 1;

            if (contentChanged) {
                // 문서 버전 저장
                DocumentVersion nextDocVer = new DocumentVersion(
                        nextVerId,
                        docId,
                        nextVerNum,
                        request.content(),
                        request.editReason(),
                        userId
                );
                documentVersionRepository.save(nextDocVer);

                doc.setLatestVersionId(nextVerId);
            }

            if (titleChanged || categoryChanged) {
                // 문서 저장
                if (titleChanged) {
                    doc.setTitle(request.title());
                }
                if (categoryChanged) {
                    doc.setCategoryId(request.categoryId());
                }
            }
            // JPA가 트랜잭션 범위 안에서 DB로부터 조회한 엔티티(doc)의 상태를 지켜보고 있음


        }
            // 결과 값
            UUID resultVersionId = contentChanged ? nextVerId : docVer.getId();
            boolean changed = titleChanged || categoryChanged || contentChanged;
            return new DocumentUpdateResponse(
                    docId,
                    resultVersionId,
                    changed
            );
    }

    @Transactional
    public void deleteDocument(UUID docId) {

        Document doc = loadDocument(docId);

        if (doc.getDeletedAt() == null) {
            doc.setDeletedAt(OffsetDateTime.now());
        }
        //이미 삭제됐을 시 그대로 종료(성공)
    }


    // 공용 함수
    // 문서 검증
    public Document loadDocument(UUID docId) {
        return documentRepository.findByIdAndDeletedAtIsNull(docId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    // 문서 버전 검증 + 최신 버전 조회
    public DocumentVersion loadDocumentVersion(UUID docId) {
        return documentVersionRepository.findFirstByDocumentIdOrderByVersionNumberDesc(docId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    // 하위 버전만 페이징하여 조회 (doc 상세 조회와 분리)
    // 추후 화면에서 문서 조회 시 하위 버전 여러개를 동시에 확인하여 버전 이동 가능
    public Page<DocumentVersion> loadDocVers(UUID docId, Pageable pageable) {
        return documentVersionRepository.findAllByDocumentId(docId, pageable)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }
}
