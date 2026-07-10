package com.acharya.dikshanta.InventoryManagement.tenant.mapper;

import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantMapper {
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "id", target = "tenantId")
    TenantResponse toResponse(Tenant tenant);
}
