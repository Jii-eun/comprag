package com.proj.compRAG.domain.document.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="document_tags")
public class DocumentTag {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "tag_id", nullable = false)
    private UUID tagId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected DocumentTag() {}

}
