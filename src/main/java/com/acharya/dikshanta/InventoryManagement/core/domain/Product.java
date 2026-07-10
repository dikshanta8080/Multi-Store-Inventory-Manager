package com.acharya.dikshanta.InventoryManagement.core.domain;

import com.acharya.dikshanta.InventoryManagement.common.enums.ProductStatus;
import com.acharya.dikshanta.InventoryManagement.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "sku"),
                @UniqueConstraint(columnNames = "barcode"),
                @UniqueConstraint(columnNames = "name")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal sellingPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    

}
