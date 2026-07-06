package com.acharya.dikshanta.InventoryManagement.common.security.service;

import com.acharya.dikshanta.InventoryManagement.common.security.SecurityUser;
import com.acharya.dikshanta.InventoryManagement.common.security.dto.AuthResponse;
import com.acharya.dikshanta.InventoryManagement.common.security.dto.LoginRequest;
import com.acharya.dikshanta.InventoryManagement.common.security.jwt.JwtService;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TenantRepository tenantRepository;

    public AuthResponse login(LoginRequest request) {
        try {
            if (request.getTenantName() != null && !request.getTenantName().isBlank()) {
                Optional<Tenant> tenantOpt = tenantRepository.findByName(request.getTenantName());
                tenantOpt.ifPresent(tenant -> TenantContext.setCurrentTenant(tenant.getSchemaName()));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityUser user = (SecurityUser) authentication.getPrincipal();

            String role = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("USER");

            String jwtToken = jwtService.generateToken(user.getUsername(), role, user.getTenantSchema());
            
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
        } finally {
            TenantContext.clear();
        }
    }
}
