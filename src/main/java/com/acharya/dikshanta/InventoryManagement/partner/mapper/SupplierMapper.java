package com.acharya.dikshanta.InventoryManagement.partner.mapper;

import com.acharya.dikshanta.InventoryManagement.partner.domain.Supplier;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.SupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierMapper {

    Supplier toEntity(SupplierCreateRequest request);

    SupplierResponse toResponse(Supplier supplier);

    void updateEntityFromRequest(SupplierUpdateRequest request, @MappingTarget Supplier supplier);
}
