package com.acharya.dikshanta.InventoryManagement.core.dto.response;

import lombok.Builder;

@Builder
public record WarehouseResponse(
        Long id,
        String name,
        String address
) {
}
