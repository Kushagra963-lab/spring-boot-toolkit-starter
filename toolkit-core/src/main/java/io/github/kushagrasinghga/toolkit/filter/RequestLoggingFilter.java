package io.github.kushagrasinghga.toolkit.filter;

import io.github.kushagrasinghga.toolkit.properties.ToolkitProperties;
import io.github.kushagrasinghga.toolkit.util.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private final ToolkitProperties properties;

    public RequestLoggingFilter(ToolkitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            String user = principalName(request.getUserPrincipal());
            log.info("{} {} status={} durationMs={} ip={} user={} responseSize={}{}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    elapsedMs,
                    RequestUtils.clientIp(request),
                    user,
                    response.getBufferSize(),
                    headers(request));
        }
    }

    private String principalName(Principal principal) {
        return principal == null ? "anonymous" : principal.getName();
    }

    private String headers(HttpServletRequest request) {
        if (!properties.getLogging().isIncludeHeaders()) {
            return "";
        }
        return " headers=" + Collections.list(request.getHeaderNames()).stream()
                .map(name -> name + "=" + Collections.list(request.getHeaders(name)))
                .toList();
    }
}
