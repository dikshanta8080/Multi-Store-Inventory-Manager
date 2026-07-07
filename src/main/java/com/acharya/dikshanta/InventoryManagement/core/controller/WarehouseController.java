package com.acharya.dikshanta.InventoryManagement.core.controller;

import com.acharya.dikshanta.InventoryManagement.common.dto.response.ApiResponse;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.WarehouseResponse;
import com.acharya.dikshanta.InventoryManagement.core.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(
            @RequestBody @Valid WarehouseCreateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Warehouse created successfully", warehouseService.createWarehouse(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAllWarehouses() {
        return ResponseEntity.ok(ApiResponse.success("Warehouses fetched successfully", warehouseService.getAllWarehouses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Warehouse fetched successfully", warehouseService.getWarehouseById(id)));
    }
}
