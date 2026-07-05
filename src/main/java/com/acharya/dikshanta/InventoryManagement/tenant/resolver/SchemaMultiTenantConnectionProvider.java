package com.acharya.dikshanta.InventoryManagement.tenant.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaMultiTenantConnectionProvider
        implements MultiTenantConnectionProvider<String> {

    private static final String DEFAULT_SCHEMA = "public";

    private static final String SCHEMA_EXISTS_QUERY =
            "SELECT 1 FROM information_schema.schemata WHERE schema_name = ?";

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

        validateTenantIdentifier(tenantIdentifier);

        Connection connection = getAnyConnection();

        try {
            ensureSchemaExists(connection, tenantIdentifier);

            try (Statement statement = connection.createStatement()) {
                log.info("Switching schema to '{}'", tenantIdentifier);

                statement.execute("""
                        SET search_path TO "%s"
                        """.formatted(tenantIdentifier));
            }

            return connection;

        } catch (SQLException exception) {

            connection.close();

            throw new SQLException(
                    "Failed to switch to tenant schema: " + tenantIdentifier,
                    exception
            );
        }
    }

    @Override
    public void releaseConnection(
            String tenantIdentifier,
            Connection connection
    ) throws SQLException {

        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                    SET search_path TO "%s"
                    """.formatted(DEFAULT_SCHEMA));

            log.debug("Reset schema back to '{}'", DEFAULT_SCHEMA);

        } catch (SQLException exception) {

            log.warn(
                    "Failed to reset search_path to '{}' before releasing connection; " +
                            "closing connection instead of returning it to the pool dirty.",
                    DEFAULT_SCHEMA, exception
            );

            throw exception;

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

        throw new IllegalArgumentException(
                "Unknown unwrap type: " + unwrapType
        );
    }

    private void validateTenantIdentifier(String tenantIdentifier)
            throws SQLException {

        if (tenantIdentifier == null || tenantIdentifier.isBlank()) {
            throw new SQLException("Tenant identifier cannot be null or blank.");
        }

        if (!tenantIdentifier.matches("^[a-zA-Z0-9_-]{1,63}$")) {
            throw new SQLException("Invalid tenant identifier: " + tenantIdentifier);
        }
    }

    private void ensureSchemaExists(Connection connection, String tenantIdentifier)
            throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(SCHEMA_EXISTS_QUERY)) {

            statement.setString(1, tenantIdentifier);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException(
                            "Tenant schema does not exist: " + tenantIdentifier
                    );
                }
            }
        }
    }
}