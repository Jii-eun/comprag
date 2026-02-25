package com.proj.comprag.domain.document.repository;

import com.proj.comprag.domain.document.entity.Document;
import com.proj.comprag.dto.document.DocumentListResponse;
import com.proj.comprag.dto.document.DocumentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    // 목록 조회 - 처음 버전
    List<DocumentListResponse> findAllByDeletedAtIsNullOrderByUpdatedAtDesc();

    // 목록 조회 + 작성자명 + 카테고리명 - projection 방식 조회
    @Query("""
        select  new com.proj.compRAG.dto.document.DocumentListResponse(
            d.id as id,
            d.title as title,
            d.categoryId as categoryId,
            coalesce(c.name, 'None') as categoryName,
            d.createdBy as createdBy,
            coalesce(u.name, 'Unknown') as userName,
            d.updatedAt as updatedAt 
        )            
        from Document d 
        left join d.user u
        left join d.category c
        where 1 = 1 
            and d.deletedAt is null
    """)
    Page<DocumentListResponse> findDocsList(Pageable pageable);

    // 목록 검색 (제목, 내용, 사용자명)
//    List<DocumentListResponse> findDocs();
//    Document findAllById(UUID id);

    // 문서 상세 조회 with content
    // 별칭 constructor expression에서는 의미가 없음. 순서+타입으로 매칭
    @Query("""
        SELECT new com.proj.compRAG.dto.document.DocumentResponse(
            d.id as id,
            d.title as title,
            d.categoryId as categoryId,
            c.name as categoryName,
            lv.content as content,
            d.createdAt as createdAt,
            lv.createdAt as updatedAt,
            d.createdBy as createdBy,
            u.name as createdByName,
            lv.createdBy as updatedBy,
            lvu.name as updatedByName,
            lv.editReason as editReason
        )
        FROM Document d
        LEFT JOIN d.category c
        LEFT JOIN d.user u
        LEFT JOIN d.latestVersion lv
        LEFT JOIN lv.user lvu        
        WHERE 1 = 1 
            AND d.deletedAt is null
            AND d.id = :docId
    """)
    Optional<DocumentResponse> findDocWithContById(UUID docId);

    // 삭제되지않은 문서 조회
    Optional<Document> findByIdAndDeletedAtIsNull(UUID docId);

}
