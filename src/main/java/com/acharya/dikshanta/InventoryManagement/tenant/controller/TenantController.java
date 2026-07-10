package com.acharya.dikshanta.InventoryManagement.tenant.controller;

import com.acharya.dikshanta.InventoryManagement.common.dto.response.ApiResponse;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants")
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<ApiResponse<TenantResponse>> createTenant(@RequestBody @Valid TenantCreateRequest request
    ) {
        TenantResponse tenant = tenantService.createTenant(request);
        return ResponseEntity.ok(ApiResponse.success("Tenant created successfully", tenant));
    }
}
