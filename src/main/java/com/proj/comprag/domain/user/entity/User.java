package com.proj.comprag.domain.user.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    // 필드 선언
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 값을 만들고 그 결과를 받아옴 - MySQL, MariaDB
    @Id
    @GeneratedValue // 자동 생성 - JPA 구현체(Hibernate)가 DB 방언(dialect)을 보고 전략을 자동선택
    @Column(name = "id",  nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "is_Admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // 생성자
    protected User() {}

    public User(String email,
                String passwordHash,
                String name,
                boolean isAdmin) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    /**
     * 특정 작업(ex. save)를 하기 전 createdAt은 아직 null
     * 작업을 실행하게 될 때 JPA가 persist를 하기 직전에
     * @PrePersist가 자동 호출되어 createdAt이 세팅됨
     * 그 후에 해당 작업(ex. INSERT)를 실행함
     *
     * @return
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    // getter
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }


}
