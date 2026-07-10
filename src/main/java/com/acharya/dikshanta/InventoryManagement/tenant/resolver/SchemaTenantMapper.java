package com.acharya.dikshanta.InventoryManagement.tenant.resolver;

import org.hibernate.context.spi.TenantSchemaMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SchemaTenantMapper implements TenantSchemaMapper<String> {

    @Override
    public @NonNull String schemaName(@NonNull String tenantIdentifier) {
        return tenantIdentifier;
    }
}
