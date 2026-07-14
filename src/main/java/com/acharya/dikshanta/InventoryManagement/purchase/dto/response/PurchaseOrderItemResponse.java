package com.acharya.dikshanta.InventoryManagement.purchase.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
