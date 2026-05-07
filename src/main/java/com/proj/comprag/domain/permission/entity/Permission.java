package com.proj.comprag.domain.permission.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "category_permissions")
public class Permission {

    @Id
    @Column(name = "id",  nullable = false)
    private UUID id;

    @Column(name = "category_id",  nullable = false)
    private UUID categoryId;

    @Column(name = "team_id",  nullable = false)
    private UUID teamId;

    @Column(name = "created_by",  nullable = false)
    private UUID createdBy;

    @Column(name = "can_view",  nullable = false)
    private Boolean canView;

    @Column(name = "can_edit",  nullable = false)
    private Boolean canEdit;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Permission() {}

}