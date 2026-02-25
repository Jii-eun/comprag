package com.proj.comprag.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="members")
public class Member {
    //setter
    @Id
    @Column(name="id", nullable = false)
    private UUID id;

    @Column(name="team_id", nullable = false)
    private String teamId;

    @Column(name="user_id", nullable = false)
    private String userId;

    @Column(name="role", nullable = false)
    private String role;

    @Column(name="created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Member() {}

    public Member(UUID id, String email, String teamId,
                  String userId, String role, OffsetDateTime createdAt) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.role = role;
        this.createdAt =  createdAt;
    }

}
