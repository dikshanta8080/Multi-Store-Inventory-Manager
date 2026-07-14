package com.acharya.dikshanta.InventoryManagement.transaction.repository;

import com.acharya.dikshanta.InventoryManagement.transaction.domain.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
}
