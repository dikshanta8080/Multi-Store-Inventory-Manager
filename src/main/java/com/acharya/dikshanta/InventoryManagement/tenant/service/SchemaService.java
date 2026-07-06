package com.acharya.dikshanta.InventoryManagement.tenant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;


@Service
@Slf4j
@RequiredArgsConstructor
public class SchemaService {
    private static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS \"%s\"";
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public void createSchema(String schemaName) {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(schemaName)
                    .defaultSchema(schemaName)
                    .createSchemas(true)
                    .locations("classpath:db/migration/tenant")
                    .load();
            flyway.migrate();
        } catch (Exception e) {
            log.error("Failed to create the schema {}", schemaName, e);
            throw new RuntimeException(e);
        }

    }
}

