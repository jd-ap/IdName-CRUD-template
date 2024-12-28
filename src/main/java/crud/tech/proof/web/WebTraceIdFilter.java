package crud.tech.proof.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebTraceIdFilter implements Filter {

    public static final String TRACE_ID = "Trace-Id";
    public static final String X_TRACE_ID = "x-Correlation-Id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String txId = (req.getHeader(X_TRACE_ID) == null)
                ? UUID.randomUUID().toString() : req.getHeader(X_TRACE_ID);
        MDC.put(TRACE_ID, txId);
        res.addHeader(X_TRACE_ID, txId);

        long startTime = System.currentTimeMillis();
        String reqUrlWithQuery = req.getRequestURI().concat(null != req.getQueryString() ? "?".concat(req.getQueryString()) : "");
        log.info("Request {} {}", String.format("%6s", req.getMethod()), reqUrlWithQuery);

        filterChain.doFilter(servletRequest, servletResponse);

        startTime = System.currentTimeMillis() - startTime;
        log.info("Response {} in {}", res.getStatus(),
                String.format("%02d:%02d.%03d", TimeUnit.MILLISECONDS.toMinutes(startTime),
                        TimeUnit.MILLISECONDS.toSeconds(startTime) % 60,
                        TimeUnit.MILLISECONDS.toMillis(startTime) % 100));
    }
}
