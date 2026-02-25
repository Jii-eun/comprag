package com.proj.comprag.domain.document.entity;

import com.proj.comprag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_versions")
@Getter
public class DocumentVersion {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "edit_reason")
    private String editReason;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false,  insertable = false, updatable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User user;



    protected DocumentVersion() {}

    public DocumentVersion(UUID verId, UUID docId, int verNum,
                           String content, String editReason,
                           OffsetDateTime createdAt, UUID createdBy) {
        this.id = verId;
        this.documentId = docId;
        this.versionNumber = verNum;
        this.content = content;
        this.editReason = editReason;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
