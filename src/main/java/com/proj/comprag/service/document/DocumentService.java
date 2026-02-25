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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
//@Transactional    // 이거 왜 안씀?
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
        // 1) 간단 검증
        if (request.title() == null || request.title().trim().isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }

        // 2) documents 테이블 저장 (title, created_by, created_at)
        UUID documentId = UUID.randomUUID();

        // categoryId는 추후에 request로 받아서 넘겨야함
        UUID categoryId = UUID.fromString("9f3d0ff3-5349-42fa-935e-83871489f682");
        UUID versionId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        Document doc = new Document(
                documentId,
                request.title().trim(),
                now,
                now,
                userId,
                categoryId
        );

        Document savedDoc = documentRepository.save(doc);

        // 3) document_versions 테이블 저장 (content, created_at, updated_at
        UUID docId = savedDoc.getId();

        DocumentVersion docVer = new DocumentVersion(
            versionId,
            docId,
            1,
            request.content(),
            request.editReason(),
            now,
            userId
        );

        documentVersionRepository.save(docVer);

        // 4) document latest_version_id 수정
        savedDoc.changeLatestVersionId(versionId);

        return docId;
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public Page<DocumentListResponse> selectDocuments(Pageable pageable) {

        /*
        // 리스트 조회 - 각 테이블 별 3번 조회
        List<Document> docs = documentRepository.findAllByDeletedAtIsNullOrderByUpdatedAtDesc();

        // 1-1) createdBy UUID 추출 (중복 제거)
        Set<UUID> userIds = docs.stream()
                .map(Document::getCreatedBy)
                .collect(Collectors.toSet());

        // 1-2) 해당 유저들에 대한 정보 조회
        List<User> users = userRepository.findAllById(userIds);

        // 1-3) createdBy UUID-User Name 매칭 Map 변환 Map<UUID, String>
        Map<UUID, String> userNameMap = users.stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        // 2-1) categoryId 추출(중복 제거)
        Set<UUID> categoryIds = docs.stream()
                .map(Document::getCategoryId)
                .collect(Collectors.toSet());

        // 2-2) 카테고리 id에 대한 정보(카테고리 name) 조회
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        // 2-3) categoryId UUID - category Name 매칭 Map 변환
        Map<UUID, String> categoryNameMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        // 3) 문서 list DTO 매핑
        List<DocumentListResponse> resultList = docs.stream()
                .map(d -> new DocumentListResponse(
                        d.getId(),
                        d.getTitle(),
                        d.getCategoryId(),
                        categoryNameMap.getOrDefault(d.getCategoryId(), "None"),
                        d.getCreatedBy(),
                        userNameMap.getOrDefault(d.getCreatedBy(), "Unknown"),
                        d.getUpdatedAt()
                )).toList();

        // fetch join 혹은 projection(DTO로 바로 조회)를 이용해서 간단히 구현 가능
        // ✅✅✅✅✅✅
        // 단순 조회를 따로 빼고, 상세조회, 수정에서 불러서 사용도 가능.....
        */

        // projection 방식 테이블 join해서 userName, categoryName을 한번에 조회
        return documentRepository.findDocsList(pageable);



    }

    // 상세 조회(w.content)
    @Transactional
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
                        now,
                        userId
                );
                documentVersionRepository.save(nextDocVer);
//                documentVersionRepository.flush();

                doc.setLatestVersionId(nextVerId);
//                documentRepository.flush();
            }

            if (titleChanged || categoryChanged) {
                // 문서 저장
                if (titleChanged) {
                    doc.setTitle(request.title());
                }
                if (categoryChanged) {
                    doc.setCategoryId(request.categoryId());
                }
                doc.setUpdatedAt(now);
            }
//                documentRepository.save(doc); // 불필요

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
    @Transactional(readOnly = true)
    public Document loadDocument(UUID docId) {
        return documentRepository.findByIdAndDeletedAtIsNull(docId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    // 문서 버전 검증 + 최신 버전 조회
    @Transactional(readOnly = true)
    public DocumentVersion loadDocumentVersion(UUID docId) {
        return documentVersionRepository.findFirstByDocumentIdOrderByVersionNumberDesc(docId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    // 하위 버전만 페이징하여 조회 (doc 상세 조회와 분리)
    // 추후 화면에서 문서 조회 시 하위 버전 여러개를 동시에 확인하여 버전 이동 가능
    @Transactional
    public Page<DocumentVersion> loadDocVers(UUID docId, Pageable pageable) {
        return documentVersionRepository.findAllByDocumentId(docId, pageable)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND));
    }
}
