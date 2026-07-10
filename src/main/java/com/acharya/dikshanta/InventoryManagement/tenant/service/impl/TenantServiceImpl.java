package com.acharya.dikshanta.InventoryManagement.tenant.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import com.acharya.dikshanta.InventoryManagement.tenant.mapper.TenantMapper;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import com.acharya.dikshanta.InventoryManagement.tenant.service.SchemaService;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantService;
import com.acharya.dikshanta.InventoryManagement.tenant.validation.TenantRegistrationValidator;
import com.acharya.dikshanta.InventoryManagement.user.domain.Role;
import com.acharya.dikshanta.InventoryManagement.user.domain.User;
import com.acharya.dikshanta.InventoryManagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final SchemaService schemaService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TenantMapper tenantMapper;
    private final TenantRegistrationValidator tenantRegistrationValidator;

    @Override
    public TenantResponse createTenant(TenantCreateRequest request) {
        tenantRegistrationValidator.validate(request);

        String schemaName = deriveSchemaName(request.tenantName());

        schemaService.createSchema(schemaName);

        try {
            return persistTenantAndUser(request, schemaName);
        } catch (Exception e) {
            log.error(
                "Schema '{}' was created but tenant record save failed. " +
                "The schema will be re-used on retry (Flyway baseline).",
                schemaName, e);
            throw e;
        }
    }

    @Transactional
    protected TenantResponse persistTenantAndUser(TenantCreateRequest request, String schemaName) {
        User user = buildUser(request);
        Tenant tenant = buildTenant(request, schemaName, user);
        Tenant savedTenant = tenantRepository.save(tenant);
        return tenantMapper.toResponse(savedTenant);
    }

    private String deriveSchemaName(String tenantName) {
        String clean = tenantName.toLowerCase().replaceAll("[^a-z0-9]", "");
        return clean + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private User buildUser(TenantCreateRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.TENANT_ADMIN)
                .build();
    }

    private Tenant buildTenant(TenantCreateRequest request, String schemaName, User user) {
        Tenant tenant = Tenant.builder()
                .name(request.tenantName())
                .schemaName(schemaName)
                .status(TenantStatus.ACTIVE)
                .user(user)
                .build();
        user.setTenant(tenant);
        return tenant;
    }
}
