package com.acharya.dikshanta.InventoryManagement.staff.service;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import com.acharya.dikshanta.InventoryManagement.staff.domain.Staff;
import com.acharya.dikshanta.InventoryManagement.staff.dto.request.StaffCreateRequest;
import com.acharya.dikshanta.InventoryManagement.staff.dto.response.StaffResponse;
import com.acharya.dikshanta.InventoryManagement.staff.repository.StaffRepository;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StaffResponse createStaff(StaffCreateRequest request) {
        String currentTenantSchema = TenantContext.getRequiredTenant();


        if (staffRepository.existsByEmail(request.email())) {
            throw new BusinessException("Staff with this email already exists");
        }

        Tenant tenant = tenantRepository.findBySchemaName(currentTenantSchema)
                .orElseThrow(() -> new BusinessException("Tenant not found"));

        Staff staff = Staff.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .position(request.position())
                .role(request.role())
                .tenant(tenant)
                .build();

        Staff savedStaff = staffRepository.save(staff);

        return StaffResponse.builder()
                .id(savedStaff.getId())
                .name(savedStaff.getName())
                .email(savedStaff.getEmail())
                .position(savedStaff.getPosition())
                .role(savedStaff.getRole().name())
                .tenantName(tenant.getName())
                .build();
    }
}

