package com.acharya.dikshanta.InventoryManagement.core.validation;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public void validateUnique(ProductCreateRequest request) {
        if (productRepository.existsBySkuOrBarcodeOrName(request.sku(), request.barcode(), request.name())) {
            throw new BusinessException("Product with this SKU, barcode, or name already exists");
        }
    }
}
