package com.acharya.dikshanta.InventoryManagement.core.dto.response;

import com.acharya.dikshanta.InventoryManagement.common.enums.ProductStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(
        Long productId,
        String productName,
        String sku,
        String barcode,
        String description,
        BigDecimal costPrice,
        BigDecimal sellingPrice,
        ProductStatus status,
        Long categoryId,
        String categoryName,
        Long warehouseId,
        String warehouseName,
        Long inventoryId,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
