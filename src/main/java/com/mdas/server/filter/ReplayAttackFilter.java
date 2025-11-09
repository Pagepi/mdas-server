package com.mdas.server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Component
@Slf4j
public class ReplayAttackFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 请求超时时间：5分钟
    private static final long REQUEST_TIMEOUT = 5 * 60 * 1000L;
    // Nonce缓存时间：10分钟（比请求超时时间长）
    private static final long NONCE_EXPIRE_MINUTES = 10L;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 检查是否需要防重放验证
        if (shouldCheckReplay(request)) {
            String nonce = request.getHeader("X-Nonce");
            String timestamp = request.getHeader("X-Timestamp");
            String signature = request.getHeader("X-Signature");

            // 验证请求头完整性
            if (!isHeadersValid(nonce, timestamp, signature)) {
                sendErrorResponse(response, "缺少必要的防重放请求头");
                return;
            }

            // 验证请求有效性
            if (!isValidRequest(nonce, timestamp)) {
                sendErrorResponse(response, "重复请求或请求已过期");
                return;
            }
        }

        // 通过验证，继续处理请求
        filterChain.doFilter(request, response);
    }

    /**
     * 检查请求头是否完整
     */
    private boolean isHeadersValid(String nonce, String timestamp, String signature) {
        return nonce != null && !nonce.trim().isEmpty() &&
                timestamp != null && !timestamp.trim().isEmpty() &&
                signature != null && !signature.trim().isEmpty();
    }

    /**
     * 验证请求有效性
     */
    private boolean isValidRequest(String nonce, String timestamp) {
        try {
            // 检查时间戳有效性
            if (!isTimestampValid(timestamp)) {
                log.warn("请求时间戳无效: nonce={}, timestamp={}", nonce, timestamp);
                return false;
            }

            // 检查nonce唯一性
            if (!isNonceUnique(nonce)) {
                log.warn("重复请求检测: nonce={}", nonce);
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("防重放验证异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证时间戳有效性
     */
    private boolean isTimestampValid(String timestampStr) {
        try {
            long timestamp = Long.parseLong(timestampStr);
            long currentTime = System.currentTimeMillis();
            long timeDiff = Math.abs(currentTime - timestamp);

            // 时间差不能超过5分钟
            return timeDiff <= REQUEST_TIMEOUT;
        } catch (NumberFormatException e) {
            log.warn("时间戳格式错误: {}", timestampStr);
            return false;
        }
    }

    /**
     * 验证nonce唯一性
     */
    private boolean isNonceUnique(String nonce) {
        try {
            String redisKey = "replay:nonce:" + nonce;

            // 使用setIfAbsent确保原子性操作
            Boolean success = redisTemplate.opsForValue().setIfAbsent(
                    redisKey,
                    "used",
                    Duration.ofMinutes(NONCE_EXPIRE_MINUTES)
            );

            return Boolean.TRUE.equals(success);
        } catch (Exception e) {
            log.error("Redis操作异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json;charset=UTF-8");

        String errorJson = String.format(
                "{\"success\":false,\"code\":400,\"message\":\"%s\",\"path\":\"%s\"}",
                message, "防重放验证"
        );

        response.getWriter().write(errorJson);
        log.warn("防重放验证失败: {}", message);
    }

    /**
     * 判断是否需要防重放验证
     */
    private boolean shouldCheckReplay(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getServletPath();

        // 对以下请求进行防重放验证：
        // 1. 所有非GET的API请求
        // 2. 排除健康检查等公共接口
        return "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) ||
                "PATCH".equals(method) && path.startsWith("/api/") &&
                        !path.equals("/api/auth/health") &&
                        !path.equals("/api/auth/login"); // 登录接口有单独的速率限制
    }
}