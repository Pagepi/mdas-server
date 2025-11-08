package com.mdas.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 日志拦截器 - 记录所有API请求的访问日志和性能数据
 */
@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 生成请求ID和记录开始时间
        long startTime = System.currentTimeMillis();
        String requestId = generateRequestId();

        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);

        // 记录请求开始日志
        logRequestStart(request, requestId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 获取请求开始时间和ID
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);

        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;

            // 记录请求完成日志
            logRequestCompletion(request, response, requestId, duration, ex);
        }
    }

    /**
     * 记录请求开始信息
     */
    private void logRequestStart(HttpServletRequest request, String requestId) {
        String requestInfo = buildRequestInfo(request, requestId);

        if (log.isDebugEnabled()) {
            // DEBUG级别记录详细请求头信息
            log.debug("[REQUEST-START] {} Headers: {}", requestInfo, getHeadersInfo(request));
        } else {
            // INFO级别记录基本信息
            log.info("[REQUEST-START] {}", requestInfo);
        }
    }

    /**
     * 记录请求完成信息
     */
    private void logRequestCompletion(HttpServletRequest request, HttpServletResponse response,
                                      String requestId, long duration, Exception ex) {
        String requestInfo = buildRequestInfo(request, requestId);
        int status = response.getStatus();

        // 获取当前用户信息（如果有）
        String userInfo = getUserInfo(request);

        // 构建日志消息
        String logMessage = String.format("[REQUEST-END] %s Status: %d Duration: %dms %s",
                requestInfo, status, duration, userInfo);

        // 根据状态码和异常情况选择日志级别
        if (ex != null) {
            log.error("{} Exception: {}", logMessage, ex.getMessage(), ex);
        } else if (status >= 500) {
            log.error("{}", logMessage);
        } else if (status >= 400) {
            log.warn("{}", logMessage);
        } else if (duration > 1000) {
            log.warn("{} ⚠️ Slow Request", logMessage);
        } else {
            log.info("{}", logMessage);
        }
    }

    /**
     * 构建请求基本信息
     */
    private String buildRequestInfo(HttpServletRequest request, String requestId) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);
        String userAgent = getShortUserAgent(request);

        String fullUri = (queryString != null) ? uri + "?" + queryString : uri;

        return String.format("ID: %s %s %s from %s (%s)",
                requestId, method, fullUri, clientIp, userAgent);
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个IP时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 获取简化的User-Agent信息
     */
    private String getShortUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown";
        }

        // 简化User-Agent显示
        if (userAgent.contains("Postman")) {
            return "Postman";
        } else if (userAgent.contains("curl")) {
            return "curl";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else {
            return userAgent.length() > 50 ? userAgent.substring(0, 50) + "..." : userAgent;
        }
    }

    /**
     * 获取请求头信息（DEBUG级别使用）
     */
    private String getHeadersInfo(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!"authorization".equalsIgnoreCase(headerName)) { // 排除敏感信息
                String headerValue = request.getHeader(headerName);
                headers.append(headerName).append(": ").append(headerValue);
                if (headerNames.hasMoreElements()) {
                    headers.append("; ");
                }
            }
        }

        return headers.toString();
    }

    /**
     * 获取用户信息（如果已认证）
     */
    private String getUserInfo(HttpServletRequest request) {
        Object currentUser = request.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        if (currentUser instanceof com.mdas.server.entity.SystemUserAccounts) {
            com.mdas.server.entity.SystemUserAccounts user =
                    (com.mdas.server.entity.SystemUserAccounts) currentUser;
            return String.format("User: %s(%s)", user.getAccount(), user.getName());
        }
        return "User: Anonymous";
    }

    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return Long.toHexString(System.currentTimeMillis()) +
                "-" + Long.toHexString(System.nanoTime() % 1000000);
    }
}