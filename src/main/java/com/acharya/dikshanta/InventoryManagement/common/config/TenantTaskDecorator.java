package com.acharya.dikshanta.InventoryManagement.common.config;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

@Component
public class TenantTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // Capture tenant at submission time (may be null for non-tenant tasks)
        String tenantId = TenantContext.getCurrentTenant();
        return () -> {
            try {
                if (tenantId != null) {
                    TenantContext.setCurrentTenant(tenantId);
                }
                runnable.run();
            } finally {
                TenantContext.clear();
            }
        };
    }
}

