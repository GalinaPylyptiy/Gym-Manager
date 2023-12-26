package com.epam.gym.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final String transactionIdKey = "transactionId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String transactionId = extractOrGenerateTransactionId(request);
        MDC.put(transactionIdKey, transactionId);
        response.addHeader(transactionIdKey, transactionId);
        logger.info("Received request: {} {} ", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Sent response: {} {} with status {} ",
                request.getMethod(), request.getRequestURI(), response.getStatus());
        MDC.remove(transactionIdKey);
    }

    private String extractOrGenerateTransactionId(final HttpServletRequest request) {
        if (request.getHeader(transactionIdKey) == null) {
            String transactionId = UUID.randomUUID().toString();
            logger.warn("The request header with transaction ID is not detected. Generating the new ID: " + transactionId);
            return transactionId;
        }
        return request.getHeader(transactionIdKey);
    }
}
