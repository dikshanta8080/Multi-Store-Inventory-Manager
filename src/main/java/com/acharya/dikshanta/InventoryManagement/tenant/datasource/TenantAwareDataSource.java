package com.acharya.dikshanta.InventoryManagement.tenant.datasource;

import com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class TenantAwareDataSource extends DelegatingDataSource {

    public TenantAwareDataSource(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return prepareConnection(super.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return prepareConnection(super.getConnection(username, password));
    }

    private Connection prepareConnection(Connection connection) throws SQLException {
        String tenant = TenantContext.getCurrentTenant();
        Connection tenantConnection = TenantSchemaUtils.openTenantConnection(connection, tenant);
        if (tenant != null && !tenant.isBlank() && !"public".equalsIgnoreCase(tenant)) {
            log.debug("Applied tenant search_path for schema '{}'", tenant);
        }
        return tenantConnection;
    }
}
