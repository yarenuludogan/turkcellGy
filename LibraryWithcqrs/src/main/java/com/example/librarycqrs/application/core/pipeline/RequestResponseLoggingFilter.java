package com.example.librarycqrs.application.core.pipeline;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
@Order(1)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Value("${performance.threshold-ms:3000}")
    private long thresholdMs;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestAndResponse(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper request,
                                       ContentCachingResponseWrapper response,
                                       long duration) {
        String requestBody = getPayload(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getPayload(response.getContentAsByteArray(), response.getCharacterEncoding());
        String authorization = request.getHeader("Authorization");
        String authMask = StringUtils.hasText(authorization) ? "Bearer [PROTECTED]" : "NONE";

        logger.info("[REQUEST] method={} uri={} auth={} payload={} | [RESPONSE] status={} payload={} | duration={}ms",
                request.getMethod(),
                request.getRequestURI(),
                authMask,
                requestBody,
                response.getStatus(),
                responseBody,
                duration
        );

        if (duration > thresholdMs) {
            logger.warn("[PERFORMANCE] slow request detected: method={} uri={} duration={}ms threshold={}ms",
                    request.getMethod(), request.getRequestURI(), duration, thresholdMs);
        }
    }

    private String getPayload(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0) {
            return "";
        }
        Charset charset = StringUtils.hasText(charsetName) ? Charset.forName(charsetName) : Charset.defaultCharset();
        String payload = new String(buf, charset);
        return payload.replaceAll("\r?\n", " ").trim();
    }
}
