package com.acharya.dikshanta.InventoryManagement.core.mapper;

import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.WarehouseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {

    @Mapping(target = "inventories", ignore = true)
    Warehouse toEntity(WarehouseCreateRequest request);

    WarehouseResponse toResponse(Warehouse warehouse);
}

