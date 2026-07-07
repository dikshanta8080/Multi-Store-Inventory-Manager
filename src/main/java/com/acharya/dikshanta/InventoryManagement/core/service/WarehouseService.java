package com.acharya.dikshanta.InventoryManagement.core.service;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.core.domain.Warehouse;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.request.WarehouseUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.core.dto.response.WarehouseResponse;
import com.acharya.dikshanta.InventoryManagement.core.mapper.WarehouseMapper;
import com.acharya.dikshanta.InventoryManagement.core.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    private static void updateExistingWarehouse(WarehouseUpdateRequest request, Warehouse warehouse) {
        if (request.name() != null && !request.name().isBlank()) {
            warehouse.setName(request.name());
        }
        if (request.address() != null && !request.address().isBlank()) {
            warehouse.setAddress(request.name());
        }
    }

    @Transactional
    public WarehouseResponse createWarehouse(WarehouseCreateRequest request) {
        checkIfAlreadyExists(request);
        Warehouse warehouse = warehouseMapper.toEntity(request);
        return warehouseMapper.toResponse(warehouseRepository.save(warehouse));
    }

    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(warehouseMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseById(Long id) {
        return warehouseMapper.toResponse(warehouseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Warehouse not found with id: " + id)));
    }

    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseUpdateRequest request) {
        Warehouse warehouse = getWarehouse(id);
        updateExistingWarehouse(request, warehouse);
        return warehouseMapper.toResponse(warehouseRepository.save(warehouse));
    }

    private @NonNull Warehouse getWarehouse(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Warehouse not found with id: " + id));
    }

    private void checkIfAlreadyExists(WarehouseCreateRequest request) {
        if (warehouseRepository.existsByName(request.name())) {
            throw new BusinessException("Warehouse already exists with this name");
        }
    }

}
