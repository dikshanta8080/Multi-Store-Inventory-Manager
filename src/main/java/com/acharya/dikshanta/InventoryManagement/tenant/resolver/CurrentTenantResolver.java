package com.acharya.dikshanta.InventoryManagement.tenant.resolver;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver<String> {

    private static final String PUBLIC_SCHEMA = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        if (tenant == null || tenant.isBlank()) {
            return PUBLIC_SCHEMA;
        }
        return tenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
