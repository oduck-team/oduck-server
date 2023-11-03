package io.oduck.api.global.security.filter;

import static io.oduck.api.global.utils.HttpHeaderUtils.getClientIP;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class ReqLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/v1/actuator/prometheus")) {
            log.info("Request: {} {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                getClientIP(request),
                request.getHeader("User-Agent")
            );
        }

        filterChain.doFilter(request, response);
    }
}
