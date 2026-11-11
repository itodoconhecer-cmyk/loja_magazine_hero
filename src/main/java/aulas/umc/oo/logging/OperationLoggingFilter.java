package aulas.umc.oo.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OperationLoggingFilter extends OncePerRequestFilter {

    private static final Logger OPERATIONS_LOG = LoggerFactory.getLogger("OPERATIONS");
    private static final String CORRELATION_HEADER = "X-Correlation-Id";
    private static final Pattern SAFE_CORRELATION_ID =
            Pattern.compile("[A-Za-z0-9._:-]{1,100}");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = normalizeCorrelationId(request.getHeader(CORRELATION_HEADER));

        long startedAt = System.nanoTime();
        MDC.put("correlationId", correlationId);
        response.setHeader(CORRELATION_HEADER, correlationId);

        OPERATIONS_LOG.info(
                "request_started method={} uri={}",
                request.getMethod(),
                request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = (System.nanoTime() - startedAt) / 1_000_000;
            OPERATIONS_LOG.info(
                    "request_finished method={} uri={} status={} durationMs={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    durationMs);
            MDC.remove("correlationId");
        }
    }

    private String normalizeCorrelationId(String value) {
        if (value == null || value.isBlank() || !SAFE_CORRELATION_ID.matcher(value).matches()) {
            return UUID.randomUUID().toString();
        }
        return value;
    }
}
