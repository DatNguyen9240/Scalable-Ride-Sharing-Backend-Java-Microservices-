package com.ridesharing.tripservice.util;

/**
 * Simple ThreadLocal holder for tenant id extracted from incoming requests.
 * Used by services/repositories to scope DB operations.
 */
public final class TenantContext {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(String tenantId) {
        CONTEXT.set(tenantId);
    }

    public static String getTenantId() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
