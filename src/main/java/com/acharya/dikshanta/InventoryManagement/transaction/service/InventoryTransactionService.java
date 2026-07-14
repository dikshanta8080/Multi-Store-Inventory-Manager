package com.acharya.dikshanta.InventoryManagement.transaction.service;

import com.acharya.dikshanta.InventoryManagement.common.enums.TransactionType;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;

public interface InventoryTransactionService {
    void recordTransaction(Product product, Warehouse warehouse, TransactionType type, Integer quantityChange, String referenceNumber, String notes);
}
