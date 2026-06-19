package com.proj.comprag.domain.document.entity;

import com.proj.comprag.domain.common.BaseTimeEntity;
import com.proj.comprag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_versions")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class DocumentVersion {

    @Id
    @Column(name = "id", nullable = false)
//    @GeneratedValue(strategy=GenerationType.UUID) //
    private UUID id;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "content")
    private String content;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;


    protected DocumentVersion() {}

    public DocumentVersion(UUID verId, UUID docId, int verNum,
                           String content, String editReason, UUID createdBy) {
        this.id = verId;
        this.documentId = docId;
        this.versionNumber = verNum;
        this.content = content;
        this.editReason = editReason;
        this.createdBy = createdBy;
    }
}
