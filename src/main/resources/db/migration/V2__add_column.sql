ALTER TABLE document_versions
ADD COLUMN edit_reason text NULL;

-- name 컬럼 있음-취소
-- ALTER TABLE team
-- ADD COLUMN team_name text NOT NULL;

--두개는 아직 entity 추가 안함