package com.mdas.server.controller;

import com.mdas.server.annotation.RateLimit;
import com.mdas.server.dto.LoginRequest;
import com.mdas.server.dto.LoginResponse;
import com.mdas.server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录接口
     * POST http://localhost:8080/api/auth/login
     */
    @PostMapping("/login")
    @RateLimit(limit = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.MINUTES,
            message = "登录尝试次数过多，请1分钟后再试")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        return authService.login(loginRequest.getAccount(), loginRequest.getPassword(), request);
    }

    /**
     * 用户登出接口
     */
    @PostMapping("/logout")
    public LoginResponse logout(HttpServletRequest request) {
        try {
            // 从请求属性获取当前用户
            Object currentUser = request.getAttribute("currentUser");
            if (currentUser instanceof com.mdas.server.entity.SystemUserAccounts) {
                com.mdas.server.entity.SystemUserAccounts user =
                        (com.mdas.server.entity.SystemUserAccounts) currentUser;

                String clientIp = getClientIp(request);
                String userAgent = request.getHeader("User-Agent");

                // 记录登出日志（这里需要注入LoginLogService）
                // loginLogService.logLogout(user, clientIp, userAgent, 0L);

                log.info("用户 {} 登出", user.getAccount());
            }

            return new LoginResponse(true, "登出成功");

        } catch (Exception e) {
            log.error("登出过程异常: {}", e.getMessage(), e);
            return new LoginResponse(false, "登出失败");
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return "认证服务运行正常";
    }

    /**
     * 获取客户端IP（辅助方法）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}