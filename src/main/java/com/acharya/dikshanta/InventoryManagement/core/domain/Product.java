package com.acharya.dikshanta.InventoryManagement.core.domain;

import com.acharya.dikshanta.InventoryManagement.common.enums.ProductStatus;
import com.acharya.dikshanta.InventoryManagement.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product extends BaseEntity {
    private String sku;
    private String barcode;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private ProductStatus status;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
