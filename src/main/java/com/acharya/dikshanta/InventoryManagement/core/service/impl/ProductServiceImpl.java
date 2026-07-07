package com.acharya.dikshanta.InventoryManagement.core.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Product;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.ProductResponse;
import com.acharya.dikshanta.InventoryManagement.core.event.ProductCreatedEvent;
import com.acharya.dikshanta.InventoryManagement.core.mapper.ProductMapper;
import com.acharya.dikshanta.InventoryManagement.core.repository.ProductRepository;
import com.acharya.dikshanta.InventoryManagement.core.service.CategoryService;
import com.acharya.dikshanta.InventoryManagement.core.service.ProductService;
import com.acharya.dikshanta.InventoryManagement.core.service.WarehouseService;
import com.acharya.dikshanta.InventoryManagement.core.validation.ProductValidator;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@RequiresTenant
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        productValidator.validateUnique(request);

        Product product = productMapper.toEntity(request);
        product.setCategory(categoryService.requireCategory(request.categoryId()));
        warehouseService.requireWarehouse(request.warehouseId());

        Product savedProduct = productRepository.save(product);

        eventPublisher.publishEvent(new ProductCreatedEvent(
                savedProduct.getId(),
                request.warehouseId(),
                TenantContext.getCurrentTenant()
        ));

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product requireProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + id));
    }
}
