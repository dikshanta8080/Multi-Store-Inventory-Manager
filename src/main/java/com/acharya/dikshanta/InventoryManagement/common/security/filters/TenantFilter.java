package com.acharya.dikshanta.InventoryManagement.common.security.filters;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String tenant = request.getHeader(TENANT_HEADER);

            if (tenant != null && !tenant.isBlank()) {
                TenantContext.setCurrentTenant(tenant);
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}