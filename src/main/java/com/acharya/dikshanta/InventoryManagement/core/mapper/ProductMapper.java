package com.acharya.dikshanta.InventoryManagement.core.mapper;

import com.acharya.dikshanta.InventoryManagement.core.domain.Inventory;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.ProductResponse;

public interface ProductMapper {

    Product toEntity(ProductCreateRequest request);

    ProductResponse toResponse(Product product);

    ProductResponse toResponse(Product product, Inventory inventory);
}
