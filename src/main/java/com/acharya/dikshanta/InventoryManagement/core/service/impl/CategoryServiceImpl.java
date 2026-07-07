package com.acharya.dikshanta.InventoryManagement.core.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Category;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.CategoryCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.CategoryResponse;
import com.acharya.dikshanta.InventoryManagement.core.mapper.CategoryMapper;
import com.acharya.dikshanta.InventoryManagement.core.repository.CategoryRepository;
import com.acharya.dikshanta.InventoryManagement.core.service.CategoryService;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@RequiresTenant
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        checkIfAlreadyExists(request);
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toResponse(requireCategory(id));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Category requireCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found with id: " + id));
    }

    private void checkIfAlreadyExists(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new BusinessException("Category with this name already exists");
        }
    }
}
