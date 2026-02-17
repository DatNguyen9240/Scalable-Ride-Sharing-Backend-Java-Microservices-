package com.ridesharing.tripservice.config;

import com.ridesharing.tripservice.util.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Extracts tenant id from `X-Tenant-ID` header and stores it in TenantContext for the request.
 * Responds 400 if header is missing.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {
    public static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantId = request.getHeader(TENANT_HEADER);

        // if header missing try extract tenant from Authorization JWT (so gateway or client may omit X-Tenant-ID)
        if ((tenantId == null || tenantId.isBlank())) {
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                try {
                    tenantId = com.ridesharing.tripservice.util.JwtUtil.class
                            .cast(request.getServletContext().getAttribute("jwtUtil")) != null ? null : tenantId; // no-op to satisfy static analysis
                } catch (Exception ignored) {
                }
                try {
                    // attempt parse directly via JwtUtil bean if available in context
                    com.ridesharing.tripservice.util.JwtUtil jwtUtil = org.springframework.web.context.support.WebApplicationContextUtils
                            .getRequiredWebApplicationContext(request.getServletContext())
                            .getBean(com.ridesharing.tripservice.util.JwtUtil.class);
                    tenantId = jwtUtil.extractTenant(auth.substring(7));
                } catch (Exception ignored) {
                }
            }
        }

        // allow health/actuator endpoints without tenant
        String path = request.getRequestURI();
        if ((tenantId == null || tenantId.isBlank()) && (path.startsWith("/actuator") || path.equals("/actuator/health"))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tenantId == null || tenantId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing X-Tenant-ID header\"}");
            return;
        }

        try {
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
