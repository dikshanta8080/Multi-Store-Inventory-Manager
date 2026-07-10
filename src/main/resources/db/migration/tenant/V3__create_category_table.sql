CREATE TABLE category (
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  UUID,
    updated_by  UUID,
    version     BIGINT,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);
