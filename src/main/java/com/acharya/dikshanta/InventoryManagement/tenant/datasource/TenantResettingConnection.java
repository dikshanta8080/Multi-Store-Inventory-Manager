package com.acharya.dikshanta.InventoryManagement.tenant.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Resets PostgreSQL search_path when the connection is returned to the pool.
 */
final class TenantResettingConnection {

    private TenantResettingConnection() {
    }

    static Connection wrap(Connection target) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[] {Connection.class},
                new ResetOnCloseHandler(target)
        );
    }

    private static final class ResetOnCloseHandler implements InvocationHandler {

        private final Connection target;

        private ResetOnCloseHandler(Connection target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("close".equals(method.getName()) && method.getParameterCount() == 0) {
                try {
                    TenantSchemaUtils.resetSearchPath(target);
                } finally {
                    target.close();
                }
                return null;
            }
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException exception) {
                Throwable cause = exception.getCause();
                if (cause instanceof SQLException sqlException) {
                    throw sqlException;
                }
                if (cause instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                if (cause instanceof Error error) {
                    throw error;
                }
                throw cause != null ? cause : exception;
            }
        }
    }
}
