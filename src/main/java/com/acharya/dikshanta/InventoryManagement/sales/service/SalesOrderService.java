package com.acharya.dikshanta.InventoryManagement.sales.service;

import com.acharya.dikshanta.InventoryManagement.sales.dto.request.SalesOrderCreateRequest;
import com.acharya.dikshanta.InventoryManagement.sales.dto.response.SalesOrderResponse;

import java.util.List;

public interface SalesOrderService {
    SalesOrderResponse createSalesOrder(SalesOrderCreateRequest request);
    SalesOrderResponse getSalesOrder(Long id);
    List<SalesOrderResponse> getAllSalesOrders();
    SalesOrderResponse fulfillSalesOrder(Long id);
    SalesOrderResponse cancelSalesOrder(Long id);
}
