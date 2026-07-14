package com.acharya.dikshanta.InventoryManagement.sales.mapper;

import com.acharya.dikshanta.InventoryManagement.sales.domain.SalesOrder;
import com.acharya.dikshanta.InventoryManagement.sales.domain.SalesOrderItem;
import com.acharya.dikshanta.InventoryManagement.sales.dto.response.SalesOrderItemResponse;
import com.acharya.dikshanta.InventoryManagement.sales.dto.response.SalesOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SalesOrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    SalesOrderResponse toResponse(SalesOrder salesOrder);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    SalesOrderItemResponse toResponse(SalesOrderItem item);
}
