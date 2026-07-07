package com.acharya.dikshanta.InventoryManagement.core.mapper;

import com.acharya.dikshanta.InventoryManagement.core.domain.Category;
import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class DefaultProductMapper implements ProductMapper {

    @Override
    public Product toEntity(ProductCreateRequest request) {
        return Product.builder()
                .name(request.name())
                .sku(request.sku())
                .barcode(request.barcode())
                .description(request.description())
                .costPrice(request.costPrice())
                .sellingPrice(request.sellingPrice())
                .status(request.status())
                .build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        return mapProductFields(product).build();
    }

    @Override
    public ProductResponse toResponse(Product product, Inventory inventory) {
        return mapProductFields(product)
                .inventoryId(inventory.getId())
                .quantity(inventory.getQuantity())
                .warehouseId(inventory.getWarehouse().getId())
                .warehouseName(inventory.getWarehouse().getName())
                .build();
    }

    private ProductResponse.ProductResponseBuilder mapProductFields(Product product) {
        Category category = product.getCategory();
        return ProductResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .description(product.getDescription())
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .status(product.getStatus())
                .categoryId(category != null ? category.getId() : null)
                .categoryName(category != null ? category.getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt());
    }
}
