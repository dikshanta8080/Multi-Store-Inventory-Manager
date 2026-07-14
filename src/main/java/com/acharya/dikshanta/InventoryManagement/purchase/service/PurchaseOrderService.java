package com.acharya.dikshanta.InventoryManagement.purchase.service;

import com.acharya.dikshanta.InventoryManagement.purchase.dto.request.PurchaseOrderCreateRequest;
import com.acharya.dikshanta.InventoryManagement.purchase.dto.response.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderCreateRequest request);
    PurchaseOrderResponse getPurchaseOrder(Long id);
    List<PurchaseOrderResponse> getAllPurchaseOrders();
    PurchaseOrderResponse receivePurchaseOrder(Long id);
    PurchaseOrderResponse cancelPurchaseOrder(Long id);
}
