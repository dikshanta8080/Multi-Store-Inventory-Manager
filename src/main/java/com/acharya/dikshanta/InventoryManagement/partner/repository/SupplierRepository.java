package com.acharya.dikshanta.InventoryManagement.partner.repository;

import com.acharya.dikshanta.InventoryManagement.partner.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
