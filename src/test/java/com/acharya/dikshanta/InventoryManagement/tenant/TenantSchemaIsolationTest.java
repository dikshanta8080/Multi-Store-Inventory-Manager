package com.acharya.dikshanta.InventoryManagement.tenant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TenantSchemaIsolationTest {

    @Autowired
    @Qualifier("rawDataSource")
    private DataSource rawDataSource;

    @Test
    void tenantSchemasShouldNotShareCategoryTablesWithPublic() throws Exception {
        List<String> tenantSchemas = new ArrayList<>();

        try (Connection connection = rawDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet tenants = statement.executeQuery(
                     "SELECT schema_name FROM public.tenants ORDER BY id")) {
            while (tenants.next()) {
                tenantSchemas.add(tenants.getString("schema_name"));
            }
        }

        assertThat(tenantSchemas).isNotEmpty();

        for (String schema : tenantSchemas) {
            try (Connection connection = rawDataSource.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet tables = statement.executeQuery(
                         "SELECT 1 FROM information_schema.tables "
                                 + "WHERE table_schema = '" + schema + "' AND table_name = 'category'")) {
                assertThat(tables.next())
                        .as("Tenant schema %s should have its own category table", schema)
                        .isTrue();
            }
        }

        try (Connection connection = rawDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet publicCategory = statement.executeQuery(
                     "SELECT 1 FROM information_schema.tables "
                             + "WHERE table_schema = 'public' AND table_name = 'category'")) {
            assertThat(publicCategory.next())
                    .as("Category table must not exist in public schema")
                    .isFalse();
        }
    }
}
