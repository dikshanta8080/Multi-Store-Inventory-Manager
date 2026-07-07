package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    Product requireProduct(Long id);
}
