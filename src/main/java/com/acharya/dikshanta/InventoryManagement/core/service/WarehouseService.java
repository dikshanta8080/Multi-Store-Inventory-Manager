package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseCreateRequest request);

    List<WarehouseResponse> getAllWarehouses();

    WarehouseResponse getWarehouseById(Long id);

    WarehouseResponse updateWarehouse(Long id, WarehouseUpdateRequest request);

    Warehouse requireWarehouse(Long id);
}
