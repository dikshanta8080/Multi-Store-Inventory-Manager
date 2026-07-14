package com.acharya.dikshanta.InventoryManagement.sales.repository;

import com.acharya.dikshanta.InventoryManagement.sales.domain.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    boolean existsByOrderNumber(String orderNumber);
}
