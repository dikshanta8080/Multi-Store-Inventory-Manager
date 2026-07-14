package com.acharya.dikshanta.InventoryManagement.purchase.repository;

import com.acharya.dikshanta.InventoryManagement.purchase.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    boolean existsByOrderNumber(String orderNumber);
}
