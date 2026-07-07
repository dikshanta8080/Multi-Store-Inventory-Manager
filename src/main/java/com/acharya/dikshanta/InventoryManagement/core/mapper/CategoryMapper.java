package com.acharya.dikshanta.InventoryManagement.core.mapper;

import com.acharya.dikshanta.InventoryManagement.core.domain.Category;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.CategoryCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreateRequest request);
    CategoryResponse toResponse(Category category);
}
