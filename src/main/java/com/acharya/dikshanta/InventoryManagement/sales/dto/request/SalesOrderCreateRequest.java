package com.acharya.dikshanta.InventoryManagement.sales.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SalesOrderCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private String shippingAddress;

    private String notes;

    @NotEmpty(message = "Sales order must have at least one item")
    @Valid
    private List<SalesOrderItemRequest> items;
}
