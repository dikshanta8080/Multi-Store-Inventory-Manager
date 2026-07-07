package com.acharya.dikshanta.InventoryManagement.tenant.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class TenantSchemaUtils {

    private static final String PUBLIC_SCHEMA = "public";

    private TenantSchemaUtils() {
    }

    public static void applySearchPath(Connection connection, String schema) throws SQLException {
        if (schema == null || schema.isBlank() || PUBLIC_SCHEMA.equalsIgnoreCase(schema)) {
            resetSearchPath(connection);
            return;
        }
        validateSchemaName(schema);
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO \"" + escapeIdentifier(schema) + "\", public");
        }
    }

    public static void resetSearchPath(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO public");
        }
    }

    public static Connection openTenantConnection(Connection connection, String tenantSchema) throws SQLException {
        resetSearchPath(connection);
        if (tenantSchema != null && !tenantSchema.isBlank() && !PUBLIC_SCHEMA.equalsIgnoreCase(tenantSchema)) {
            applySearchPath(connection, tenantSchema);
        }
        return TenantResettingConnection.wrap(connection);
    }

    private static void validateSchemaName(String schema) throws SQLException {
        if (!schema.matches("^[a-zA-Z0-9_-]{1,63}$")) {
            throw new SQLException("Invalid tenant schema name: " + schema);
        }
    }

    private static String escapeIdentifier(String identifier) {
        return identifier.replace("\"", "\"\"");
    }
}
