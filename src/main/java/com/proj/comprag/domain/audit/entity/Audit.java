package com.proj.comprag.domain.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class Audit {

    @Id
    @Column(name = "id",  nullable = false)
    private UUID id;

    @Column(name = "actor_user_id",  nullable = false)
    private UUID actorUserId;

    @Column(name = "action",  nullable = false)
    private String action;

    @Column(name = "resource_type",  nullable = false)
    private String resourceType;

    @Column(name = "resource_id",  nullable = false)
    private UUID resourceId;   // permission 로그이므로 category_permission_id

    //  json 설정
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", nullable = false)
    private String metadata;    // DB는 JSONB임 확인 필요

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Audit() {}

}

