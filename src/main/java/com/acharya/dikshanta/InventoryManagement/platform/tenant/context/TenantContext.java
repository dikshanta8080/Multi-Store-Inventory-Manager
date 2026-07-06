package com.acharya.dikshanta.InventoryManagement.platform.tenant.context;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;

public final class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
    }

    /**
     * Returns the current tenant schema, or null if not set.
     */
    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    /**
     * Returns the current tenant schema or throws if null / equals "public".
     * Use this in service methods that require an active tenant context.
     */
    public static String getRequiredTenant() {
        String tenant = currentTenant.get();
        if (tenant == null || tenant.isBlank() || "public".equalsIgnoreCase(tenant)) {
            throw new BusinessException("This operation requires an active tenant context. Please log in as a tenant user.");
        }
        return tenant;
    }

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static void clear() {
        currentTenant.remove();
    }
}

