package com.rideshare.commons.web;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that runs at the very start of the filter chain (before Spring Security).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Stamps every request with a unique {@code requestId} (taken from the
 *       {@code X-Request-ID} header if the caller already set one, otherwise generated).</li>
 *   <li>Echoes the {@code requestId} back in the response so clients can correlate
 *       their requests to log/trace entries.</li>
 *   <li>Guarantees {@link MDC#clear()} in a {@code finally} block so no context
 *       leaks across thread-pool reuse.</li>
 * </ul>
 * <p>
 * Auth-related MDC fields ({@code userId}, {@code userEmail}) are added separately by
 * {@link com.rideshare.commons.security.JwtAuthFilter} once the JWT is validated.
 * The {@code traceId} / {@code spanId} fields are injected automatically by Micrometer Tracing.
 * All MDC fields are emitted as top-level JSON properties by {@code LogstashEncoder}.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter extends OncePerRequestFilter {

    static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        MDC.put(MDC_REQUEST_ID, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String header = request.getHeader(REQUEST_ID_HEADER);
        return (header != null && !header.isBlank()) ? header : UUID.randomUUID().toString();
    }
}
