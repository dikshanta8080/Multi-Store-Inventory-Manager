package com.acharya.dikshanta.InventoryManagement.core.repository;

import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
