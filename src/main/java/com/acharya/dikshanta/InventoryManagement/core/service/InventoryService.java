package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductRestockRequest;

public interface InventoryService {

    Inventory createInitialInventory(Long productId, Long warehouseId);

    Inventory getByProductId(Long productId);


    void restockProduct(ProductRestockRequest request);

    void deductStock(com.acharya.dikshanta.InventoryManagement.core.dto.request.InventoryDeductRequest request);
}
