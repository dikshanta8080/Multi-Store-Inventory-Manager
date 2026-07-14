package com.acharya.dikshanta.InventoryManagement.purchase.mapper;

import com.acharya.dikshanta.InventoryManagement.purchase.domain.PurchaseOrder;
import com.acharya.dikshanta.InventoryManagement.purchase.domain.PurchaseOrderItem;
import com.acharya.dikshanta.InventoryManagement.purchase.dto.response.PurchaseOrderItemResponse;
import com.acharya.dikshanta.InventoryManagement.purchase.dto.response.PurchaseOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PurchaseOrderMapper {

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    PurchaseOrderResponse toResponse(PurchaseOrder purchaseOrder);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    PurchaseOrderItemResponse toResponse(PurchaseOrderItem item);
}
