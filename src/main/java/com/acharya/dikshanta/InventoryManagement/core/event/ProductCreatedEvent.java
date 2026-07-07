package com.acharya.dikshanta.InventoryManagement.core.event;

public record ProductCreatedEvent(
        Long productId,
        Long warehouseId,
        String tenantSchema
) {
}
