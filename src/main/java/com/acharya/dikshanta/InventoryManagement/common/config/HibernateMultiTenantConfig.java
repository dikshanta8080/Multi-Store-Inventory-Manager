package com.acharya.dikshanta.InventoryManagement.common.config;

import com.acharya.dikshanta.InventoryManagement.tenant.resolver.CurrentTenantResolver;
import com.acharya.dikshanta.InventoryManagement.tenant.resolver.SchemaMultiTenantConnectionProvider;
import com.acharya.dikshanta.InventoryManagement.tenant.resolver.SchemaTenantMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.MultiTenancySettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HibernateMultiTenantConfig {

    private static final String PUBLIC_SCHEMA = "public";

    private final SchemaMultiTenantConnectionProvider connectionProvider;
    private final CurrentTenantResolver tenantResolver;
    private final SchemaTenantMapper tenantSchemaMapper;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return (Map<String, Object> properties) -> {
            properties.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
            properties.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
            properties.put(MultiTenancySettings.MULTI_TENANT_SCHEMA_MAPPER, tenantSchemaMapper);
            properties.put(MultiTenancySettings.TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY, PUBLIC_SCHEMA);
        };
    }
}
