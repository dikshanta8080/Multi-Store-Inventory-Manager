package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Category;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.CategoryCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.CategoryResponse;
import com.acharya.dikshanta.InventoryManagement.core.mapper.CategoryMapper;
import com.acharya.dikshanta.InventoryManagement.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        checkIfAlreadyExists(request);
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    private void checkIfAlreadyExists(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new BusinessException("Category with this name already exists");
        }
    }

    List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
