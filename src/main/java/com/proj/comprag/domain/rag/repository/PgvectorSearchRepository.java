package com.proj.comprag.domain.rag.repository;

import com.proj.comprag.domain.rag.entity.DocumentChunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PgvectorSearchRepository {

    // JdbcTemplate에서 Repository = 직접 만든 DAO (DB 접근 책임 클래스)

    private final JdbcTemplate jdbcTemplate;

    public PgvectorSearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DocumentChunk> search(float[] embedding) {

        String sql = """
            SELECT id, document_id, chunk_index, content
            FROM document_chunks
            ORDER BY embedding <=> ?
            LIMIT 5
        """;

        return jdbcTemplate.query(sql,
                new Object[]{embedding},
                (rs, rowNum) -> DocumentChunk.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .documentId(UUID.fromString(rs.getString("document_id")))
                        .chunkIndex(rs.getInt("chunk_index"))
                        .content(rs.getString("content"))
                        .build()
        );
    }



}
