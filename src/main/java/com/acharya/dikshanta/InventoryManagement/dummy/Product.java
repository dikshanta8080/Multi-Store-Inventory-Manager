package com.acharya.dikshanta.InventoryManagement.dummy;

import com.acharya.dikshanta.InventoryManagement.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product extends BaseEntity {
    private String name;
    private double price;
}
