package com.acharya.dikshanta.InventoryManagement.platform.tenant.context;

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

    public static void clear() {
        currentTenant.remove();
    }
}
