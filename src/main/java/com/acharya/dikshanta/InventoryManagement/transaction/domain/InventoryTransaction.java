package com.acharya.dikshanta.InventoryManagement.transaction.domain;

import com.acharya.dikshanta.InventoryManagement.common.enums.TransactionType;
import com.acharya.dikshanta.InventoryManagement.common.model.BaseEntity;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "reference_number")
    private String referenceNumber; // SO-1234 or PO-5678

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
