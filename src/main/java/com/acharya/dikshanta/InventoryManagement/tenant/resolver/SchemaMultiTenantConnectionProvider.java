package com.acharya.dikshanta.InventoryManagement.tenant.resolver;

import com.acharya.dikshanta.InventoryManagement.tenant.datasource.TenantSchemaUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private static final String PUBLIC_SCHEMA = "public";

    private static final String SCHEMA_EXISTS_QUERY =
            "SELECT 1 FROM information_schema.schemata WHERE schema_name = ?";

    private final DataSource dataSource;

    public SchemaMultiTenantConnectionProvider(@Qualifier("rawDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return TenantSchemaUtils.openTenantConnection(dataSource.getConnection(), PUBLIC_SCHEMA);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        validateTenantIdentifier(tenantIdentifier);

        Connection connection = dataSource.getConnection();

        try {
            ensureSchemaExists(connection, tenantIdentifier);
            Connection tenantConnection = TenantSchemaUtils.openTenantConnection(connection, tenantIdentifier);
            log.debug("Switched JDBC search_path to tenant '{}'", tenantIdentifier);
            return tenantConnection;
        } catch (SQLException exception) {
            connection.close();
            throw new SQLException("Failed to switch to tenant schema: " + tenantIdentifier, exception);
        }
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean handlesConnectionSchema() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return unwrapType.isAssignableFrom(getClass())
                || unwrapType.isAssignableFrom(MultiTenantConnectionProvider.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) this;
        }
        throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType);
    }

    private void validateTenantIdentifier(String tenantIdentifier) throws SQLException {
        if (tenantIdentifier == null || tenantIdentifier.isBlank()) {
            throw new SQLException("Tenant identifier cannot be null or blank.");
        }
        if (!tenantIdentifier.matches("^[a-zA-Z0-9_-]{1,63}$")) {
            throw new SQLException("Invalid tenant identifier: " + tenantIdentifier);
        }
    }

    private void ensureSchemaExists(Connection connection, String tenantIdentifier) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SCHEMA_EXISTS_QUERY)) {
            statement.setString(1, tenantIdentifier);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Tenant schema does not exist: " + tenantIdentifier);
                }
            }
        }
    }
}
