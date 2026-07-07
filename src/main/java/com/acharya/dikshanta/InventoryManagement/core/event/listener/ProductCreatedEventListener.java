package com.acharya.dikshanta.InventoryManagement.core.event.listener;

import com.acharya.dikshanta.InventoryManagement.core.event.ProductCreatedEvent;
import com.acharya.dikshanta.InventoryManagement.core.service.InventoryService;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCreatedEventListener {

    private final InventoryService inventoryService;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        try {
            TenantContext.setCurrentTenant(event.tenantSchema());
            inventoryService.createInitialInventory(event.productId(), event.warehouseId());
            log.info("Initial inventory created for product {} in warehouse {}", event.productId(), event.warehouseId());
        } catch (Exception e) {
            log.error("Failed to create initial inventory for product {} in warehouse {}",
                    event.productId(), event.warehouseId(), e);
        } finally {
            TenantContext.clear();
        }
    }
}
