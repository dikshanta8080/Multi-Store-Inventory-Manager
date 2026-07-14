package com.acharya.dikshanta.InventoryManagement.partner.mapper;

import com.acharya.dikshanta.InventoryManagement.partner.domain.Customer;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    Customer toEntity(CustomerCreateRequest request);

    CustomerResponse toResponse(Customer customer);

    void updateEntityFromRequest(CustomerUpdateRequest request, @MappingTarget Customer customer);
}
