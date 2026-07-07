package com.acharya.dikshanta.InventoryManagement.tenant.validation;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import com.acharya.dikshanta.InventoryManagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantRegistrationValidator {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public void validate(TenantCreateRequest request) {
        if (userRepository.existsByEmail(request.email()) || userRepository.existsByUsername(request.username())) {
            throw new BusinessException("User already exists");
        }
        if (tenantRepository.existsByName(request.tenantName())) {
            throw new BusinessException("Tenant already exists");
        }
    }
}
