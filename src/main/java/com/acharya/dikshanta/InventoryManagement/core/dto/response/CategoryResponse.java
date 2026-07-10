package com.acharya.dikshanta.InventoryManagement.core.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description
) {
}
