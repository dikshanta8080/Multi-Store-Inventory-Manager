package com.acharya.dikshanta.InventoryManagement.tenant.mapper;

import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {
    public TenantResponse toResponse(Tenant tenant) {
        return TenantResponse.builder()
                .userName(tenant.getUser().getUsername())
                .email(tenant.getUser().getEmail())
                .tenantId(tenant.getId())
                .name(tenant.getName())
                .schemaName(tenant.getSchemaName())
                .status(tenant.getStatus())
                .build();
    }
}
