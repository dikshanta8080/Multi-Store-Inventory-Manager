package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;

public interface InventoryService {

    Inventory createInitialInventory(Long productId, Long warehouseId);

    Inventory getByProductId(Long productId);
}
