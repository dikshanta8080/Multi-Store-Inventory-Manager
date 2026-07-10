package com.acharya.dikshanta.InventoryManagement.tenant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchemaService {

    private final @Qualifier("rawDataSource") DataSource rawDataSource;

    public void createSchema(String schemaName) {
        log.info("Provisioning schema '{}' with Flyway migrations", schemaName);
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(rawDataSource)
                    .schemas(schemaName)
                    .defaultSchema(schemaName)
                    .createSchemas(true)
                    .locations("classpath:db/migration/tenant")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            log.info("Schema '{}' provisioned successfully", schemaName);
        } catch (Exception e) {
            log.error("Failed to provision schema '{}'", schemaName, e);
            throw new RuntimeException("Schema provisioning failed for: " + schemaName, e);
        }
    }
}
