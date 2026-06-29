package com.proj.comprag.domain.rag.entity;

import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "document_chunks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunk {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    // 벡터 검색 성능과 단순성을 위해 FK는 유지하되 JPA 연관관계는 사용하지 않음
    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @Column(name = "content")
    private String content;

    @Column(name = "embedding", columnDefinition = "vector(768)")
    private PGvector embedding;

//    저장방식으로 JDBC Template을 사용하면 해당 필드 선택적 관리 가능
//    @Transient //DB컬럼이 아니라 저장처리 안됨.
//    private float[] embedding;


//    ** DocuemntCuhnk table field
//    document_id UUID NOT NULL REFERENCES documents(id),
//    chunk_index INT NOT NULL,
//    content TEXT NOT NULL NOT NULL,
//    embedding VECTOR(768)


}
