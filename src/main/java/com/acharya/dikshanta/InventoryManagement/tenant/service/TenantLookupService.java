package com.acharya.dikshanta.InventoryManagement.tenant.service;

import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;

public interface TenantLookupService {

    Tenant requireBySchemaName(String schemaName);
}
