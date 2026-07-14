package com.acharya.dikshanta.InventoryManagement.transaction.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.enums.TransactionType;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.transaction.domain.InventoryTransaction;
import com.acharya.dikshanta.InventoryManagement.transaction.repository.InventoryTransactionRepository;
import com.acharya.dikshanta.InventoryManagement.transaction.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;

    @Override
    @Transactional
    public void recordTransaction(Product product, Warehouse warehouse, TransactionType type, Integer quantityChange, String referenceNumber, String notes) {
        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(product)
                .warehouse(warehouse)
                .transactionType(type)
                .quantityChange(quantityChange)
                .referenceNumber(referenceNumber)
                .notes(notes)
                .build();
        transactionRepository.save(transaction);
    }
}
