package com.acharya.dikshanta.InventoryManagement.purchase.dto.response;

import com.acharya.dikshanta.InventoryManagement.common.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponse {
    private Long id;
    private String orderNumber;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private PurchaseOrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime expectedDeliveryDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PurchaseOrderItemResponse> items;
}
