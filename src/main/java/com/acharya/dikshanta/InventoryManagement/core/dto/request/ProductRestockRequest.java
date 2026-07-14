package com.acharya.dikshanta.InventoryManagement.core.dto.request;

public record ProductRestockRequest(
        Long productId,
        Long warehouseId,
        Integer quantity
) {
}
