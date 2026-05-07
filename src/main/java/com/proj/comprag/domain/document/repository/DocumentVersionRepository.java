package com.proj.comprag.domain.document.repository;

import com.proj.comprag.domain.document.entity.DocumentVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, UUID> {

    Optional<DocumentVersion> findTopByDocumentIdOrderByVersionNumberDesc(UUID id);

    Optional<DocumentVersion> findFirstByDocumentIdOrderByVersionNumberDesc(UUID id);

    Optional<Page<DocumentVersion>> findAllByDocumentId(UUID id, Pageable pageable);
}
