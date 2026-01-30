-- CREATE
CREATE TABLE "categories" (
                              "id"	uuid		NOT NULL,
                              "name"	varchar(100)		NOT NULL,
                              "description"	text		NULL,
                              "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                              "updated_at"	timestamptz	DEFAULT null	NULL
);

CREATE TABLE "audit_logs" (
                              "id"	uuid		NOT NULL,
                              "action"	varchar		NOT NULL,
                              "resource_type"	varchar		NULL,
                              "resource_id"	uuid		NULL,
                              "metadata"	jsonb		NULL,
                              "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                              "actor_user_id"	uuid		NOT NULL
);

COMMENT ON COLUMN "audit_logs"."resource_type" IS 'category_permission
우선 카테고리 권한 변경만 기록 후에 추가로 기록할게 생기면 추가될 수 있음';

COMMENT ON COLUMN "audit_logs"."resource_id" IS '범용 로그라 FK 안 걸어도 된다고 함';

CREATE TABLE "documents" (
                             "id"	uuid		NOT NULL,
                             "title"	varchar(255)		NOT NULL,
                             "deleted_at"	timestamptz	DEFAULT null	NULL,
                             "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                             "updated_at"	timestamptz	DEFAULT null	NULL,
                             "created_by"	uuid		NOT NULL,
                             "latest_version_id"	uuid		NULL,
                             "category_id"	uuid		NOT NULL
);

CREATE TABLE "users" (
                         "id"	uuid		NOT NULL,
                         "email"	varchar		NOT NULL,
                         "name"	varchar		NOT NULL,
                         "password_hash"	varchar		NULL,
                         "is_admin"	boolean	DEFAULT false	NOT NULL,
                         "created_at"	timestamptz	DEFAULT now()	NOT NULL
);

COMMENT ON COLUMN "users"."password_hash" IS '소셜로그인/SSO 대비';

CREATE TABLE "tags" (
                        "id"	uuid		NOT NULL,
                        "name"	varchar(100)		NOT NULL,
                        "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                        "updated_at"	timestamptz	DEFAULT null	NULL
);

CREATE TABLE "teams" (
                         "id"	uuid		NOT NULL,
                         "name"	varchar		NOT NULL,
                         "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                         "updated_at"	timestamptz		NULL
);


CREATE TABLE "document_versions" (
                                     "id"	uuid		NOT NULL,
                                     "version_number"	int	DEFAULT 1	NOT NULL,
                                     "content"	text		NOT NULL,
                                     "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                                     "document_id"	uuid		NOT NULL,
                                     "created_by"	uuid		NOT NULL
);


CREATE TABLE "document_tags" (
                                 "tag_id"	uuid		NOT NULL,
                                 "document_id"	uuid		NOT NULL,
                                 "created_at"	timestamptz	DEFAULT now()	NOT NULL
);

CREATE TABLE "team_members" (
                                "id"	uuid		NOT NULL,
                                "role"	varchar		NULL,
                                "created_at"	timestamptz	DEFAULT now()	NULL,
                                "user_id"	uuid		NOT NULL,
                                "team_id"	uuid		NOT NULL
);

CREATE TABLE "indexing_jobs" (
                                 "id"	uuid		NOT NULL,
                                 "status"	varchar		NOT NULL,
                                 "attempts"	int	DEFAULT 0	NOT NULL,
                                 "last_error"	text		NULL,
                                 "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                                 "processed_at"	timestamptz	DEFAULT null	NULL,
                                 "document_version_id"	uuid		NOT NULL
);

CREATE TABLE "category_permissions" (
                                        "id"	uuid		NOT NULL,
                                        "can_view"	boolean	DEFAULT true	NOT NULL,
                                        "can_edit"	boolean	DEFAULT false	NOT NULL,
                                        "created_at"	timestamptz	DEFAULT now()	NOT NULL,
                                        "updated_at"	timestamptz	DEFAULT null	NULL,
                                        "category_id"	uuid		NOT NULL,
                                        "created_by"	uuid		NOT NULL,
                                        "team_id"	uuid		NOT NULL
);


-- constraint
ALTER TABLE "categories" ADD CONSTRAINT "PK_CATEGORIES" PRIMARY KEY (
                                                                     "id"
    );

ALTER TABLE "audit_logs" ADD CONSTRAINT "PK_AUDIT_LOGS" PRIMARY KEY (
                                                                     "id"
    );

ALTER TABLE "documents" ADD CONSTRAINT "PK_DOCUMENTS" PRIMARY KEY (
                                                                   "id"
    );

ALTER TABLE "users" ADD CONSTRAINT "PK_USERS" PRIMARY KEY (
                                                           "id"
    );

ALTER TABLE "tags" ADD CONSTRAINT "PK_TAGS" PRIMARY KEY (
                                                         "id"
    );

ALTER TABLE "teams" ADD CONSTRAINT "PK_TEAMS" PRIMARY KEY (
                                                           "id"
    );

ALTER TABLE "document_versions" ADD CONSTRAINT "PK_DOCUMENT_VERSIONS" PRIMARY KEY (
                                                                                   "id"
    );

ALTER TABLE "document_tags" ADD CONSTRAINT "PK_DOCUMENT_TAGS" PRIMARY KEY (
                                                                           "tag_id",
                                                                           "document_id"
    );

ALTER TABLE "team_members" ADD CONSTRAINT "PK_TEAM_MEMBERS" PRIMARY KEY (
                                                                         "id"
    );

ALTER TABLE "indexing_jobs" ADD CONSTRAINT "PK_INDEXING_JOBS" PRIMARY KEY (
                                                                           "id"
    );

ALTER TABLE "category_permissions" ADD CONSTRAINT "PK_CATEGORY_PERMISSIONS" PRIMARY KEY (
                                                                                         "id"
    );

ALTER TABLE "audit_logs" ADD CONSTRAINT "FK_users_TO_audit_logs_1" FOREIGN KEY (
                                                                                "actor_user_id"
    )
    REFERENCES "users" (
                        "id"
        );

ALTER TABLE "documents" ADD CONSTRAINT "FK_users_TO_documents_1" FOREIGN KEY (
                                                                              "created_by"
    )
    REFERENCES "users" (
                        "id"
        );

ALTER TABLE "documents" ADD CONSTRAINT "FK_document_versions_TO_documents_1" FOREIGN KEY (
                                                                                          "latest_version_id"
    )
    REFERENCES "document_versions" (
                                    "id"
        );

ALTER TABLE "documents" ADD CONSTRAINT "FK_categories_TO_documents_1" FOREIGN KEY (
                                                                                   "category_id"
    )
    REFERENCES "categories" (
                             "id"
        );

ALTER TABLE "document_versions" ADD CONSTRAINT "FK_documents_TO_document_versions_1" FOREIGN KEY (
                                                                                                  "document_id"
    )
    REFERENCES "documents" (
                            "id"
        );

ALTER TABLE "document_versions" ADD CONSTRAINT "FK_users_TO_document_versions_1" FOREIGN KEY (
                                                                                              "created_by"
    )
    REFERENCES "users" (
                        "id"
        );

ALTER TABLE "document_tags" ADD CONSTRAINT "FK_tags_TO_document_tags_1" FOREIGN KEY (
                                                                                     "tag_id"
    )
    REFERENCES "tags" (
                       "id"
        );

ALTER TABLE "document_tags" ADD CONSTRAINT "FK_documents_TO_document_tags_1" FOREIGN KEY (
                                                                                          "document_id"
    )
    REFERENCES "documents" (
                            "id"
        );

ALTER TABLE "team_members" ADD CONSTRAINT "FK_users_TO_team_members_1" FOREIGN KEY (
                                                                                    "user_id"
    )
    REFERENCES "users" (
                        "id"
        );

ALTER TABLE "team_members" ADD CONSTRAINT "FK_teams_TO_team_members_1" FOREIGN KEY (
                                                                                    "team_id"
    )
    REFERENCES "teams" (
                        "id"
        );

ALTER TABLE "indexing_jobs" ADD CONSTRAINT "FK_document_versions_TO_indexing_jobs_1" FOREIGN KEY (
                                                                                                  "document_version_id"
    )
    REFERENCES "document_versions" (
                                    "id"
        );

ALTER TABLE "category_permissions" ADD CONSTRAINT "FK_categories_TO_category_permissions_1" FOREIGN KEY (
                                                                                                         "category_id"
    )
    REFERENCES "categories" (
                             "id"
        );

ALTER TABLE "category_permissions" ADD CONSTRAINT "FK_users_TO_category_permissions_1" FOREIGN KEY (
                                                                                                    "created_by"
    )
    REFERENCES "users" (
                        "id"
        );

ALTER TABLE "category_permissions" ADD CONSTRAINT "FK_teams_TO_category_permissions_1" FOREIGN KEY (
                                                                                                    "team_id"
    )
    REFERENCES "teams" (
                        "id"
        );


-- unique
ALTER TABLE users ADD CONSTRAINT uq_users_email UNIQUE (email);
ALTER TABLE categories ADD CONSTRAINT uq_categories_name UNIQUE (name);
ALTER TABLE tags ADD CONSTRAINT uq_tags_name UNIQUE (name);
ALTER TABLE teams ADD CONSTRAINT uq_teams_name UNIQUE (name);


ALTER TABLE team_members ADD CONSTRAINT uq_team_members_team_user UNIQUE (team_id, user_id);
ALTER TABLE category_permissions ADD CONSTRAINT uq_category_team UNIQUE (category_id, team_id);
ALTER TABLE document_versions ADD CONSTRAINT uq_document_versions UNIQUE (document_id, version_number);


-- index
CREATE INDEX idx_documents_category_id ON documents(category_id);
CREATE INDEX idx_documents_created_by ON documents(created_by);
CREATE INDEX idx_document_versions_document_id ON document_versions(document_id);
CREATE INDEX idx_document_tags_document_id ON document_tags(document_id);
CREATE INDEX idx_document_tags_tag_id ON document_tags(tag_id);
CREATE INDEX idx_team_members_user_id ON team_members(user_id);
CREATE INDEX idx_team_members_team_id ON team_members(team_id);
CREATE INDEX idx_category_permissions_category_id ON category_permissions(category_id);
CREATE INDEX idx_category_permissions_team_id ON category_permissions(team_id);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor_user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_indexing_jobs_status_created_at ON indexing_jobs (status, created_at);













