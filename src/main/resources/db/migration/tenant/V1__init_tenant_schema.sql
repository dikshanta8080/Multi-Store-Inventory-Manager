CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by UUID,
    updated_by UUID,
    version BIGINT,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);
