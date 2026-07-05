package com.acharya.dikshanta.InventoryManagement.common.config;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

@Component
public class TenantTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        String tenantId = TenantContext.getCurrentTenant();
        return () -> {
            try {
                TenantContext.setCurrentTenant(tenantId);
                runnable.run();
            } finally {
                TenantContext.clear();
            }
        };
    }
}
