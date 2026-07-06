CREATE TABLE public.users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by UUID,
    updated_by UUID,
    version BIGINT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    tenant_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_user_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants (id) ON DELETE CASCADE
);
