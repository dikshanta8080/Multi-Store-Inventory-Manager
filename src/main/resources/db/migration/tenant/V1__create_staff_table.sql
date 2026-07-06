CREATE TABLE staff (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by UUID,
    updated_by UUID,
    version BIGINT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    tenant_id BIGINT NOT NULL,
    CONSTRAINT fk_staff_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants (id) ON DELETE CASCADE
);
