package com.acharya.dikshanta.InventoryManagement.core.dto.request;

public record InventoryDeductRequest(
        Long productId,
        Long warehouseId,
        Integer quantity
) {
}
