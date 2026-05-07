package com.proj.comprag.domain.document.entity;

import com.proj.comprag.domain.category.entity.Category;
import com.proj.comprag.domain.common.BaseTimeEntity;
import com.proj.comprag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="documents")
@Getter
public class Document extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name="created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "latest_version_id")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID latestVersionId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    // 문서 버전 list까지 한번에 조회하려고 했으나..
    // 문서 버전을 페이징까지 넣으면 에러가 날 확률이 아주 높아져서 별도 조회로
//    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
//    private List<DocumentVersion> versions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_version_id", insertable = false, updatable = false)
    private DocumentVersion latestVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;


    protected Document() {}

    public Document(String title, UUID createdBy,
                    UUID categoryId) {
        this.title = title;
        this.createdBy = createdBy;
        this.categoryId = categoryId;
    }

    // @getter로 대체. @setter는 권장X
//    public UUID getId() { return id;}
//    public String getTitle() {return title;}


    public void changeLatestVersionId(UUID versionId) {this.latestVersionId = versionId;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public void setDeletedAt(OffsetDateTime now) {
        this.deletedAt = now;
    }

    public void setLatestVersionId(UUID verId) { this.latestVersionId = verId;}

//    public void setUpdatedAt(OffsetDateTime now) {this.updatedAt = now;}
}


