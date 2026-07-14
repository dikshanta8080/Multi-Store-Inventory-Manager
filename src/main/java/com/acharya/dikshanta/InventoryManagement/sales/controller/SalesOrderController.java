package com.acharya.dikshanta.InventoryManagement.sales.controller;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant;
import com.acharya.dikshanta.InventoryManagement.sales.dto.request.SalesOrderCreateRequest;
import com.acharya.dikshanta.InventoryManagement.sales.dto.response.SalesOrderResponse;
import com.acharya.dikshanta.InventoryManagement.sales.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
@RequiresTenant
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<SalesOrderResponse> createSalesOrder(@Valid @RequestBody SalesOrderCreateRequest request) {
        return new ResponseEntity<>(salesOrderService.createSalesOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<SalesOrderResponse> getSalesOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getSalesOrder(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SalesOrderResponse>> getAllSalesOrders() {
        return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
    }

    @PostMapping("/{id}/fulfill")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SalesOrderResponse> fulfillSalesOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.fulfillSalesOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SalesOrderResponse> cancelSalesOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.cancelSalesOrder(id));
    }
}
