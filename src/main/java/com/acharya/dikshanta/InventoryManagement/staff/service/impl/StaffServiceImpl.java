package com.acharya.dikshanta.InventoryManagement.staff.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.annotation.RequiresTenant;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import com.acharya.dikshanta.InventoryManagement.staff.domain.Staff;
import com.acharya.dikshanta.InventoryManagement.staff.dto.request.StaffCreateRequest;
import com.acharya.dikshanta.InventoryManagement.staff.dto.response.StaffResponse;
import com.acharya.dikshanta.InventoryManagement.staff.repository.StaffRepository;
import com.acharya.dikshanta.InventoryManagement.staff.service.StaffService;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@RequiresTenant
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final TenantLookupService tenantLookupService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffResponse createStaff(StaffCreateRequest request) {
        if (staffRepository.existsByEmail(request.email())) {
            throw new BusinessException("Staff with this email already exists");
        }

        Tenant tenant = tenantLookupService.requireBySchemaName(TenantContext.getCurrentTenant());

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
