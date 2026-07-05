package com.acharya.dikshanta.InventoryManagement.common.config;

import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import com.acharya.dikshanta.InventoryManagement.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TenantMigrationRunner implements CommandLineRunner {

    private final DataSource dataSource;
    private final TenantRepository tenantRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Tenant> tenants = tenantRepository.findByStatus(TenantStatus.ACTIVE);
        for (Tenant tenant : tenants) {
            String schemaName = tenant.getSchemaName();
            Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration/tenant")
                    .schemas(schemaName)
                    .load()
                    .migrate();
        }
    }
}
