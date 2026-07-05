package com.acharya.dikshanta.InventoryManagement.tenant.resolver;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private static final String DEFAULT_SCHEMA = "public";

    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        if (tenantIdentifier == null || !tenantIdentifier.matches("^[a-zA-Z0-9_-]{1,63}$")) {
            throw new SQLException("Invalid tenant identifier");
        }

        Connection connection = getAnyConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO \"" + tenantIdentifier + "\"");
        } catch (SQLException ex) {
            connection.close();
            throw new SQLException(
                    "Could not switch to schema: " + tenantIdentifier,
                    ex
            );
        }

        return connection;
    }

    @Override
    public void releaseConnection(
            String tenantIdentifier,
            Connection connection
    ) throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO \"" + DEFAULT_SCHEMA + "\"");
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
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
}