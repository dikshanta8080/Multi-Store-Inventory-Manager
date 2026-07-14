package com.acharya.dikshanta.InventoryManagement.sales.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.enums.SalesOrderStatus;
import com.acharya.dikshanta.InventoryManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.core.repository.ProductRepository;
import com.acharya.dikshanta.InventoryManagement.core.repository.WarehouseRepository;
import com.acharya.dikshanta.InventoryManagement.core.service.InventoryService;
import com.acharya.dikshanta.InventoryManagement.partner.domain.Customer;
import com.acharya.dikshanta.InventoryManagement.partner.repository.CustomerRepository;
import com.acharya.dikshanta.InventoryManagement.sales.domain.SalesOrder;
import com.acharya.dikshanta.InventoryManagement.sales.domain.SalesOrderItem;
import com.acharya.dikshanta.InventoryManagement.sales.dto.request.SalesOrderCreateRequest;
import com.acharya.dikshanta.InventoryManagement.sales.dto.response.SalesOrderResponse;
import com.acharya.dikshanta.InventoryManagement.sales.mapper.SalesOrderMapper;
import com.acharya.dikshanta.InventoryManagement.sales.repository.SalesOrderRepository;
import com.acharya.dikshanta.InventoryManagement.sales.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import com.acharya.dikshanta.InventoryManagement.transaction.service.InventoryTransactionService;
import com.acharya.dikshanta.InventoryManagement.common.enums.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final InventoryService inventoryService;
    private final InventoryTransactionService transactionService;

    @Override
    @Transactional
    public SalesOrderResponse createSalesOrder(SalesOrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        SalesOrder order = SalesOrder.builder()
                .orderNumber("SO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(customer)
                .warehouse(warehouse)
                .status(SalesOrderStatus.CONFIRMED)
                .shippingAddress(request.getShippingAddress())
                .notes(request.getNotes())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            BigDecimal totalPrice = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(totalPrice);

            SalesOrderItem item = SalesOrderItem.builder()
                    .salesOrder(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .totalPrice(totalPrice)
                    .build();
            order.getItems().add(item);
        }

        order.setTotalAmount(totalAmount);
        SalesOrder savedOrder = salesOrderRepository.save(order);
        return salesOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getSalesOrder(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));
        return salesOrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getAllSalesOrders() {
        return salesOrderRepository.findAll().stream()
                .map(salesOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalesOrderResponse fulfillSalesOrder(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        if (order.getStatus() != SalesOrderStatus.CONFIRMED) {
            throw new IllegalStateException("Can only fulfill an order that is in CONFIRMED status.");
        }

        order.setStatus(SalesOrderStatus.FULFILLED);
        SalesOrder savedOrder = salesOrderRepository.save(order);

        // Deduct inventory
        for (SalesOrderItem item : savedOrder.getItems()) {
            com.acharya.dikshanta.InventoryManagement.core.dto.request.InventoryDeductRequest deductRequest = 
                new com.acharya.dikshanta.InventoryManagement.core.dto.request.InventoryDeductRequest(
                    item.getProduct().getId(), 
                    savedOrder.getWarehouse().getId(), 
                    item.getQuantity()
                );
            inventoryService.deductStock(deductRequest);
            
            transactionService.recordTransaction(
                item.getProduct(), 
                savedOrder.getWarehouse(), 
                TransactionType.SALE, 
                -item.getQuantity(), 
                savedOrder.getOrderNumber(), 
                "Fulfilled via Sales Order"
            );
        }

        return salesOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public SalesOrderResponse cancelSalesOrder(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        if (order.getStatus() == SalesOrderStatus.FULFILLED) {
            throw new IllegalStateException("Cannot cancel an already fulfilled order.");
        }

        order.setStatus(SalesOrderStatus.CANCELLED);
        return salesOrderMapper.toResponse(salesOrderRepository.save(order));
    }
}
