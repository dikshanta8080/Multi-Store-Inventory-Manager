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
                @UniqueConstraint(name = "uk_product_sku", columnNames = "sku"),
                @UniqueConstraint(name = "uk_product_barcode", columnNames = "barcode"),
                @UniqueConstraint(name = "uk_product_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_product_category", columnList = "category_id"),
                @Index(name = "idx_product_status", columnList = "status"),
                @Index(name = "idx_product_category_status", columnList = "category_id, status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @Column(name = "barcode", nullable = false, length = 100)
    private String barcode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cost_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal sellingPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private Category category;
}