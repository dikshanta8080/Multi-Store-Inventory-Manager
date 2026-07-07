package com.acharya.dikshanta.InventoryManagement.platform.tenant.aspect;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantContextAspect {

    @Before("@within(com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant) || "
            + "@annotation(com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant)")
    public void requireActiveTenant() {
        TenantContext.getRequiredTenant();
    }
}
