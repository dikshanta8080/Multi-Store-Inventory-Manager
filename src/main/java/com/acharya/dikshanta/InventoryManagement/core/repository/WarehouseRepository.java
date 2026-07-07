package com.acharya.dikshanta.InventoryManagement.core.repository;

import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByName(String name);

    boolean existsByName(String name);
}
