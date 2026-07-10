package com.acharya.dikshanta.InventoryManagement.tenant.dto.response;

import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import lombok.Builder;

@Builder
public record TenantResponse(
        String userName,
        String email,
        Long tenantId,
        String name,
        String schemaName,
        TenantStatus status
) {
}
