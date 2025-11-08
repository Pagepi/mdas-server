package com.mdas.server.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 登录日志表 - 记录用户登录登出行为
 */
@Entity
@Table(name = "system_login_logs")
@Data
public class SystemLoginLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 64)
    private String traceId;

    // 用户信息
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_account", nullable = false, length = 50)
    private String userAccount;

    @Column(name = "user_name", length = 100)
    private String userName;

    // 登录信息
    @Column(name = "login_type", nullable = false, length = 20)
    private String loginType = "password"; // password, token, sso

    @Column(name = "login_status", nullable = false, length = 20)
    private String loginStatus; // success, failure, locked

    @Column(name = "failure_reason", length = 100)
    private String failureReason; // wrong_password, user_not_found, account_locked

    // 客户端信息
    @Column(name = "client_ip", nullable = false, length = 45)
    private String clientIp;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "client_type", length = 50)
    private String clientType; // web, mobile, desktop

    // 地理位置信息
    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "city", length = 100)
    private String city;

    // 登录详情
    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "session_duration")
    private Long sessionDuration;

    // 安全相关信息
    @Column(name = "is_suspicious")
    private Boolean isSuspicious = false;

    @Column(name = "suspicious_reason", length = 200)
    private String suspiciousReason;

    @PrePersist
    protected void onCreate() {
        if (loginTime == null) {
            loginTime = LocalDateTime.now();
        }
        if (loginType == null) {
            loginType = "password";
        }
        if (isSuspicious == null) {
            isSuspicious = false;
        }
    }

    // 便捷方法
    public boolean isSuccess() {
        return "success".equals(loginStatus);
    }

    public boolean isFailure() {
        return "failure".equals(loginStatus);
    }

    public boolean isLocked() {
        return "locked".equals(loginStatus);
    }

    // 计算会话时长
    public void calculateSessionDuration() {
        if (logoutTime != null && loginTime != null) {
            this.sessionDuration = java.time.Duration.between(loginTime, logoutTime).getSeconds();
        }
    }

    // 标记为可疑登录
    public void markAsSuspicious(String reason) {
        this.isSuspicious = true;
        this.suspiciousReason = reason;
    }

    // 获取登录状态显示名称
    public String getLoginStatusDisplay() {
        switch (loginStatus) {
            case "success": return "成功";
            case "failure": return "失败";
            case "locked": return "锁定";
            default: return loginStatus;
        }
    }

    // 获取失败原因显示名称
    public String getFailureReasonDisplay() {
        if (failureReason == null) return "";
        switch (failureReason) {
            case "wrong_password": return "密码错误";
            case "user_not_found": return "用户不存在";
            case "account_locked": return "账户已锁定";
            case "token_expired": return "Token已过期";
            case "token_invalid": return "Token无效";
            default: return failureReason;
        }
    }
}