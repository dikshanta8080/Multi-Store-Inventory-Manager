package com.acharya.dikshanta.InventoryManagement.tenant.service;

import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;

public interface TenantService {

    TenantResponse createTenant(TenantCreateRequest request);
}
