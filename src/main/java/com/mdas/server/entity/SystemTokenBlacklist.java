package com.mdas.server.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JWT Token黑名单表 - 记录已注销但尚未过期的token
 */
@Entity
@Table(name = "system_token_blacklist")
@Data
public class SystemTokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Token信息
    @Column(name = "token_hash", nullable = false, length = 128)
    private String tokenHash;

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_account", nullable = false, length = 50)
    private String userAccount;

    // 时间信息
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    // 注销信息
    @Column(name = "revoked_by")
    private Integer revokedBy;

    @Column(name = "revoked_by_account", length = 50)
    private String revokedByAccount;

    @Column(name = "revoke_reason", nullable = false, length = 100)
    private String revokeReason = "logout"; // logout, security, admin, password_change, account_locked

    // 附加信息
    @Column(name = "client_ip", length = 45)
    private String clientIp;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @PrePersist
    protected void onCreate() {
        if (blacklistedAt == null) {
            blacklistedAt = LocalDateTime.now();
        }
        if (revokeReason == null) {
            revokeReason = "logout";
        }
    }

    // 便捷方法
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean willExpireSoon(int minutes) {
        return expiresAt.isBefore(LocalDateTime.now().plusMinutes(minutes));
    }

    // 获取剩余有效时间（秒）
    public long getRemainingSeconds() {
        return java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
    }

    // 获取注销原因显示名称
    public String getRevokeReasonDisplay() {
        switch (revokeReason) {
            case "logout": return "主动登出";
            case "security": return "安全原因";
            case "admin": return "管理员操作";
            case "password_change": return "密码变更";
            case "account_locked": return "账户锁定";
            default: return revokeReason;
        }
    }

    // 生成token哈希（用于快速查询）
    public static String generateTokenHash(String token) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // 降级方案：使用简单的哈希
            return String.valueOf(token.hashCode());
        }
    }

    // 检查token是否在黑名单中（通过哈希比较）
    public static boolean isTokenBlacklisted(String tokenHash) {
        // 这个方法需要在Service中实现具体的数据库查询
        // 这里只是定义接口
        return false;
    }
}