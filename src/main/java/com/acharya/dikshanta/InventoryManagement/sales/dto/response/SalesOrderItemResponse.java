package com.acharya.dikshanta.InventoryManagement.sales.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
