package com.acharya.dikshanta.InventoryManagement.partner.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.partner.domain.Supplier;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.SupplierUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.SupplierResponse;
import com.acharya.dikshanta.InventoryManagement.partner.mapper.SupplierMapper;
import com.acharya.dikshanta.InventoryManagement.partner.repository.SupplierRepository;
import com.acharya.dikshanta.InventoryManagement.partner.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierCreateRequest request) {
        if (supplierRepository.existsByName(request.getName())) {
            throw new BusinessException("Supplier with name " + request.getName() + " already exists");
        }
        if (request.getEmail() != null && supplierRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Supplier with email " + request.getEmail() + " already exists");
        }

        Supplier supplier = supplierMapper.toEntity(request);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = getSupplier(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierUpdateRequest request) {
        Supplier supplier = getSupplier(id);

        if (request.getName() != null && !request.getName().equals(supplier.getName()) && supplierRepository.existsByName(request.getName())) {
            throw new BusinessException("Supplier with name " + request.getName() + " already exists");
        }
        if (request.getEmail() != null && !request.getEmail().equals(supplier.getEmail()) && supplierRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Supplier with email " + request.getEmail() + " already exists");
        }

        supplierMapper.updateEntityFromRequest(request, supplier);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updatedSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
    }
}
