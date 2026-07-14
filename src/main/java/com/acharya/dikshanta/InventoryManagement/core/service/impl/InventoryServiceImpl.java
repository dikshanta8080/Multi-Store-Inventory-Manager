package com.acharya.dikshanta.InventoryManagement.core.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.InventoryDeductRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductRestockRequest;
import com.acharya.dikshanta.InventoryManagement.core.repository.InventoryRepository;
import com.acharya.dikshanta.InventoryManagement.core.service.InventoryService;
import com.acharya.dikshanta.InventoryManagement.core.service.ProductService;
import com.acharya.dikshanta.InventoryManagement.core.service.WarehouseService;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@RequiresTenant
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @Override
    @Transactional
    public Inventory createInitialInventory(Long productId, Long warehouseId) {
        if (inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
            return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseThrow(() -> new BusinessException("Inventory record missing for product: " + productId));
        }

        Inventory inventory = Inventory.builder()
                .product(productService.requireProduct(productId))
                .warehouse(warehouseService.requireWarehouse(warehouseId))
                .quantity(0)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("Inventory not found for product id: " + productId));
    }


    @Override
    public void restockProduct(ProductRestockRequest request) {
        Inventory inventory = getInventory(request);
        validateQuantity(request.quantity());
        inventory.setQuantity(inventory.getQuantity() + request.quantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        
    }

    @Transactional
    public void deductStock(InventoryDeductRequest request) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(request.productId(), request.warehouseId()).orElseThrow(() ->
                new ResourceNotFoundException("Inventory not found"));
        validateQuantity(request.quantity());
        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        inventoryRepository.save(inventory);

    }

    private void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity can not be less than 1");
        }
    }

    private @NonNull Inventory getInventory(ProductRestockRequest request) {
        return inventoryRepository.findByProductIdAndWarehouseId(request.productId(), request.warehouseId()).orElseThrow(() -> new ResourceNotFoundException("Inventory does not exists"));
    }


}
