package com.mdas.server.service;

import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.SystemUserAccountRepository;
import com.mdas.server.dto.LoginResponse;
import com.mdas.server.util.JwtUtil;
import com.mdas.server.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private SystemUserAccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginLogService loginLogService;

    @Value("${login.max-fail-count:5}")
    private int maxLoginFailCount;

    /**
     * 用户登录（集成登录日志）
     */
    public LoginResponse login(String account, String password, HttpServletRequest request) {
        String traceId = generateTraceId();
        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        try {
            // 1. 基本参数验证
            if (account == null || account.trim().isEmpty()) {
                loginLogService.logLoginFailure(account, "password", "empty_account",
                        clientIp, userAgent, traceId);
                return new LoginResponse(false, "账号不能为空");
            }

            if (password == null || password.trim().isEmpty()) {
                loginLogService.logLoginFailure(account, "password", "empty_password",
                        clientIp, userAgent, traceId);
                return new LoginResponse(false, "密码不能为空");
            }

            // 2. 根据账号查找用户
            Optional<SystemUserAccounts> userOpt = userRepository.findByAccount(account);

            if (userOpt.isEmpty()) {
                loginLogService.logLoginFailure(account, "password", "user_not_found",
                        clientIp, userAgent, traceId);
                return new LoginResponse(false, "账号不存在");
            }

            SystemUserAccounts user = userOpt.get();

            // 3. 检查用户状态
            if ("locked".equals(user.getStatus())) {
                loginLogService.logAccountLocked(user, clientIp, userAgent, traceId);
                return new LoginResponse(false, "账号已被锁定，请联系管理员");
            }

            if (!"active".equals(user.getStatus())) {
                loginLogService.logLoginFailure(account, "password", "account_inactive",
                        clientIp, userAgent, traceId);
                return new LoginResponse(false, "账号状态异常: " + user.getStatus());
            }

            // 4. 检查登录失败次数
            if (user.getLoginFailCount() != null && user.getLoginFailCount() >= maxLoginFailCount) {
                user.setStatus("locked");
                userRepository.save(user);
                loginLogService.logAccountLocked(user, clientIp, userAgent, traceId);
                return new LoginResponse(false, "登录失败次数过多，账户已被锁定");
            }

            // 5. 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                // 增加失败次数
                user.setLoginFailCount(user.getLoginFailCount() == null ? 1 : user.getLoginFailCount() + 1);
                userRepository.save(user);

                loginLogService.logLoginFailure(account, "password", "wrong_password",
                        clientIp, userAgent, traceId);
                return new LoginResponse(false, "密码错误");
            }

            // 6. 登录成功
            // 重置失败次数和更新最后登录时间
            user.setLoginFailCount(0);
            user.setLastLoginTime(java.time.LocalDateTime.now());
            userRepository.save(user);

            // 7. 生成JWT token
            String token = jwtUtil.generateToken(
                    user.getId(),
                    user.getAccount(),
                    user.getName(),
                    user.getRole() != null ? user.getRole() : "user"
            );

            // 8. 记录成功日志
            loginLogService.logLoginSuccess(user, "password", clientIp, userAgent,
                    determineClientType(userAgent), traceId);

            // 9. 检查可疑登录
            boolean isSuspicious = loginLogService.checkSuspiciousLogin(user, clientIp);

            // 10. 构建响应
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getAccount(),
                    user.getName(),
                    user.getEmail(),
                    user.getMobile()
            );

            LoginResponse response = new LoginResponse(true, "登录成功", userInfo);
            response.setToken(token);
            response.setSuspicious(isSuspicious);

            if (isSuspicious) {
                response.setMessage("登录成功（检测到可疑登录，请确认是否是本人操作）");
            }

            log.info("用户 {} 登录成功, IP: {}", user.getAccount(), clientIp);
            return response;

        } catch (Exception e) {
            log.error("登录过程异常: {}", e.getMessage(), e);
            loginLogService.logLoginFailure(account, "password", "system_error",
                    clientIp, userAgent, traceId);
            return new LoginResponse(false, "登录失败，系统异常");
        }
    }

    /**
     * 获取客户端IP
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
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 判断客户端类型
     */
    private String determineClientType(String userAgent) {
        if (userAgent == null) return "unknown";

        if (userAgent.contains("Mobile")) {
            return "mobile";
        } else if (userAgent.contains("Postman")) {
            return "postman";
        } else if (userAgent.contains("curl")) {
            return "curl";
        } else {
            return "web";
        }
    }

    /**
     * 生成追踪ID
     */
    private String generateTraceId() {
        return "LOGIN_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}