package com.acharya.dikshanta.InventoryManagement.tenant.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantLookupServiceImpl implements TenantLookupService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional(readOnly = true)
    public Tenant requireBySchemaName(String schemaName) {
        return tenantRepository.findBySchemaName(schemaName)
                .orElseThrow(() -> new BusinessException("Tenant not found"));
    }
}
