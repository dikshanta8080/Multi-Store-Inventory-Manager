CREATE TABLE warehouse (
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by UUID,
    updated_by UUID,
    version    BIGINT,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255),
    CONSTRAINT uq_warehouse_name UNIQUE (name)
);
