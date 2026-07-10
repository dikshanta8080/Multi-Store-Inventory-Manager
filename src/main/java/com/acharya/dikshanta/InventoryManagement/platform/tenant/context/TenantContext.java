package com.acharya.dikshanta.InventoryManagement.platform.tenant.context;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;

public final class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getRequiredTenant() {
        String tenant = currentTenant.get();
        if (tenant == null || tenant.isBlank() || "public".equalsIgnoreCase(tenant)) {
            throw new BusinessException("This operation requires an active tenant context. Please log in as a tenant user.");
        }
        return tenant;
    }

    public static void clear() {
        currentTenant.remove();
    }
}

