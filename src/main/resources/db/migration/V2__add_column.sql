ALTER TABLE "documents"
    ADD CONSTRAINT "FK_document_versions_TO_documents_latest_version"
    FOREIGN KEY ("latest_version_id")
    REFERENCES "document_versions" ("id");
