package com.acharya.dikshanta.InventoryManagement.core.dto.request;

import com.acharya.dikshanta.InventoryManagement.common.enums.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "Product name is required")
        String name,

        @NotBlank(message = "SKU is required")
        String sku,

        @NotBlank(message = "Barcode is required")
        String barcode,

        String description,

        @NotNull(message = "Cost price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Cost price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Invalid cost price")
        BigDecimal costPrice,

        @NotNull(message = "Selling price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Invalid selling price")
        BigDecimal sellingPrice,

        @NotNull(message = "Product status is required")
        ProductStatus status,

        @NotNull(message = "Category is required")
        @Positive(message = "Category ID must be positive")
        Long categoryId,

        @NotNull(message = "Warehouse is required")
        @Positive(message = "Warehouse ID must be positive")
        Long warehouseId

) {
}
