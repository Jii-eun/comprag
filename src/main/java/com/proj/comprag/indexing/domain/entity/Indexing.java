package com.proj.comprag.indexing.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "indexing_jobs")
public class Indexing {

    // 필드 선언
    @Id
    @Column(name = "id",  nullable = false)
    private UUID id;

    @Column(name = "document_version_id",  nullable = false)
    private UUID documentVersionId;

    @Column(name = "status",  nullable = false)
    private String status;

    @Column(name = "attempts",  nullable = false)
    private int attempts;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    protected Indexing() {}

}
