package com.acharya.dikshanta.InventoryManagement;

import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
public class TenantCreationTest {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testCreateTenant() {
        TenantCreateRequest request = new TenantCreateRequest("testuser", "test@test.com", "password", "Test Tenant");
        TenantResponse response = tenantService.createTenant(request);
        assertNotNull(response);
    }
}
