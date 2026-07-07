package com.acharya.dikshanta.InventoryManagement.tenant;

import com.acharya.dikshanta.InventoryManagement.core.dto.request.CategoryCreateRequest;
import com.acharya.dikshanta.InventoryManagement.core.service.CategoryService;
import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.request.TenantCreateRequest;
import com.acharya.dikshanta.InventoryManagement.tenant.dto.response.TenantResponse;
import com.acharya.dikshanta.InventoryManagement.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class CategoryTenantIsolationTest {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void categoriesShouldBeIsolatedPerTenantSchema() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        TenantResponse tenantA = tenantService.createTenant(new TenantCreateRequest(
                "tenantA_" + suffix,
                "tenantA_" + suffix + "@test.com",
                "password",
                "Tenant A " + suffix
        ));

        TenantResponse tenantB = tenantService.createTenant(new TenantCreateRequest(
                "tenantB_" + suffix,
                "tenantB_" + suffix + "@test.com",
                "password",
                "Tenant B " + suffix
        ));

        String categoryA = "Category-A-" + suffix;
        String categoryB = "Category-B-" + suffix;

        TenantContext.setCurrentTenant(tenantA.schemaName());
        try {
            categoryService.createCategory(new CategoryCreateRequest(categoryA, "Tenant A category"));
            assertThat(categoryService.getAllCategories())
                    .map(category -> category.name())
                    .contains(categoryA)
                    .doesNotContain(categoryB);
        } finally {
            TenantContext.clear();
        }

        TenantContext.setCurrentTenant(tenantB.schemaName());
        try {
            categoryService.createCategory(new CategoryCreateRequest(categoryB, "Tenant B category"));
            assertThat(categoryService.getAllCategories())
                    .map(category -> category.name())
                    .contains(categoryB)
                    .doesNotContain(categoryA);
        } finally {
            TenantContext.clear();
        }

        TenantContext.setCurrentTenant(tenantA.schemaName());
        try {
            assertThat(categoryService.getAllCategories())
                    .map(category -> category.name())
                    .contains(categoryA)
                    .doesNotContain(categoryB);
        } finally {
            TenantContext.clear();
        }
    }
}
