package com.acharya.dikshanta.InventoryManagement.partner.service;

import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierCreateRequest request);
    SupplierResponse getSupplierById(Long id);
    List<SupplierResponse> getAllSuppliers();
    SupplierResponse updateSupplier(Long id, SupplierUpdateRequest request);
    void deleteSupplier(Long id);
}
