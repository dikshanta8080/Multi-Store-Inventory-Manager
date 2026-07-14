package com.acharya.dikshanta.InventoryManagement.purchase.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.enums.PurchaseOrderStatus;
import com.acharya.dikshanta.InventoryManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.core.repository.ProductRepository;
import com.acharya.dikshanta.InventoryManagement.core.repository.WarehouseRepository;
import com.acharya.dikshanta.InventoryManagement.core.service.InventoryService;
import com.acharya.dikshanta.InventoryManagement.partner.domain.Supplier;
import com.acharya.dikshanta.InventoryManagement.partner.repository.SupplierRepository;
import com.acharya.dikshanta.InventoryManagement.purchase.domain.PurchaseOrder;
import com.acharya.dikshanta.InventoryManagement.purchase.domain.PurchaseOrderItem;
import com.acharya.dikshanta.InventoryManagement.purchase.dto.request.PurchaseOrderCreateRequest;
import com.acharya.dikshanta.InventoryManagement.purchase.dto.response.PurchaseOrderResponse;
import com.acharya.dikshanta.InventoryManagement.purchase.mapper.PurchaseOrderMapper;
import com.acharya.dikshanta.InventoryManagement.purchase.repository.PurchaseOrderRepository;
import com.acharya.dikshanta.InventoryManagement.purchase.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import com.acharya.dikshanta.InventoryManagement.transaction.service.InventoryTransactionService;
import com.acharya.dikshanta.InventoryManagement.common.enums.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final InventoryService inventoryService;
    private final InventoryTransactionService transactionService;

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderCreateRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        PurchaseOrder order = PurchaseOrder.builder()
                .orderNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .supplier(supplier)
                .warehouse(warehouse)
                .status(PurchaseOrderStatus.ORDERED)
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .notes(request.getNotes())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            BigDecimal totalPrice = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(totalPrice);

            PurchaseOrderItem item = PurchaseOrderItem.builder()
                    .purchaseOrder(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .totalPrice(totalPrice)
                    .build();
            order.getItems().add(item);
        }

        order.setTotalAmount(totalAmount);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);
        return purchaseOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));
        return purchaseOrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(purchaseOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderResponse receivePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        if (order.getStatus() != PurchaseOrderStatus.ORDERED) {
            throw new IllegalStateException("Can only receive an order that is in ORDERED status.");
        }

        order.setStatus(PurchaseOrderStatus.RECEIVED);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // Update inventory
        for (PurchaseOrderItem item : savedOrder.getItems()) {
            com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductRestockRequest restockRequest = 
                new com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductRestockRequest(
                    item.getProduct().getId(), 
                    savedOrder.getWarehouse().getId(), 
                    item.getQuantity()
                );
            inventoryService.restockProduct(restockRequest);
            
            transactionService.recordTransaction(
                item.getProduct(), 
                savedOrder.getWarehouse(), 
                TransactionType.PURCHASE, 
                item.getQuantity(), 
                savedOrder.getOrderNumber(), 
                "Received via Purchase Order"
            );
        }

        return purchaseOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse cancelPurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        if (order.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new IllegalStateException("Cannot cancel an already received order.");
        }

        order.setStatus(PurchaseOrderStatus.CANCELLED);
        return purchaseOrderMapper.toResponse(purchaseOrderRepository.save(order));
    }
}
