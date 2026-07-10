package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.core.domain.Category;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.CategoryCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreateRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    void deleteCategory(Long id);

    Category requireCategory(Long id);
}
