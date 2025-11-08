package com.mdas.server.interceptor;

import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.UserService;
import com.mdas.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证拦截器 - 验证JWT Token并设置当前用户信息
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // 认证相关的常量
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CURRENT_USER_ATTRIBUTE = "currentUser";
    public static final String TOKEN_ATTRIBUTE = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("=== 拦截器被调用 ===");
//        log.info("请求: {} {}", request.getMethod(), request.getRequestURI());
//        log.info("Handler: {}", handler);

        // 跳过公开接口
        if (isPublicEndpoint(request)) {
            log.debug("跳过公开接口: {} {}", request.getMethod(), request.getRequestURI());
            return true;
        }

        // 获取Authorization头
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("缺少或格式错误的Authorization头: {}", authHeader);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "缺少认证Token");
            return false;
        }

        // 提取Token
        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            // 验证Token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Token验证失败: {}", jwtUtil.maskToken(token));
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token验证失败");
                return false;
            }

            // 从Token中获取用户ID
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // 从数据库查询完整用户信息
            Optional<SystemUserAccounts> currentUserOpt = userService.getUserById(userId);
            if (currentUserOpt == null) {
                log.warn("用户不存在: userId={}", userId);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "用户不存在");
                return false;
            }

            SystemUserAccounts currentUser = currentUserOpt.get();

            // 检查用户状态
            if (!"active".equals(currentUser.getStatus())) {
                log.warn("用户状态异常: userId={}, status={}", userId, currentUser.getStatus());
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "用户状态异常: " + currentUser.getStatus());
                return false;
            }

            // 将用户信息和Token存入request属性
            request.setAttribute(CURRENT_USER_ATTRIBUTE, currentUser);
            request.setAttribute(TOKEN_ATTRIBUTE, token);

            log.debug("认证成功: user={}({}), uri={}",
                    currentUser.getAccount(), currentUser.getName(), request.getRequestURI());

            return true;

        } catch (RuntimeException e) {
            log.error("认证过程异常: {} - {}", request.getRequestURI(), e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "认证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查是否为公开接口（不需要认证）
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 登录接口
        if (path.startsWith("/api/auth/")) {
            return true;
        }

        // 公开API文档接口（如果有）
        if (path.startsWith("/swagger") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars/")) {
            return true;
        }

        // 健康检查接口
        if (path.equals("/actuator/health") || path.equals("/health")) {
            return true;
        }

        // 测试接口（开发环境）
        if (path.startsWith("/api/test/")) {
            return true;
        }

        // 静态资源
        if (path.startsWith("/static/") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            return true;
        }

        return false;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("code", status);

        String jsonResponse = convertMapToJson(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    /**
     * 简单的Map转JSON（避免引入额外依赖）
     */
    private String convertMapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }
}