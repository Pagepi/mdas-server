package com.mdas.server.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统操作日志表 - 记录重要的业务操作
 */
@Entity
@Table(name = "system_operation_logs")
@Data
public class SystemOperationLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 64)
    private String traceId;

    @Column(name = "module", nullable = false, length = 50)
    private String module; // user, system, auth

    @Column(name = "action", nullable = false, length = 100)
    private String action; // create, update, delete, export, import

    @Column(name = "operation_content", nullable = false, columnDefinition = "TEXT")
    private String operationContent;

    // 操作人信息
    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "operator_account", length = 50)
    private String operatorAccount;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "operator_ip", length = 45)
    private String operatorIp;

    // 操作目标信息
    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "target_type", length = 50)
    private String targetType; // user, role, config

    @Column(name = "target_content", columnDefinition = "TEXT")
    private String targetContent;

    // 变更详情
    @Column(name = "change_before", columnDefinition = "TEXT")
    private String changeBefore;

    @Column(name = "change_after", columnDefinition = "TEXT")
    private String changeAfter;

    @Column(name = "change_fields", columnDefinition = "TEXT")
    private String changeFields;

    // 操作结果
    @Column(name = "operation_status", nullable = false, length = 20)
    private String operationStatus = "success"; // success, failure

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // 时间信息
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    @Column(name = "cost_time")
    private Long costTime;

    /**
     * 受影响的数据详情（JSON格式，用于记录关键数据变更）
     */
    @Column(name = "change_details", columnDefinition = "TEXT")
    private String changeDetails;

    /**
     * 操作来源：web, mobile, api 等
     */
    @Column(name = "operation_source", length = 50)
    private String operationSource;

    /**
     * 会话ID
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;

    /**
     * 地理位置信息
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 设备类型：desktop, mobile, tablet
     */
    @Column(name = "device_type", length = 50)
    private String deviceType;

    /**
     * 浏览器信息
     */
    @Column(name = "browser_info", length = 200)
    private String browserInfo;

    /**
     * 安全级别：low, medium, high, critical
     */
    @Column(name = "security_level", length = 20)
    private String securityLevel;

    // 创建时间
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 创建时间
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 操作状态：success, failure
    @Column(name = "status", length = 20)
    private String status;

    @PrePersist
    protected void onCreate() {
        if (operationTime == null) {
            operationTime = LocalDateTime.now();
        }
        if (operationStatus == null) {
            operationStatus = "success";
        }
    }

    public SystemOperationLogs() {
        this.operationTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "success";
        this.operationSource = "web";
        this.securityLevel = "medium";
    }

    // 便捷方法
    public boolean isSuccess() {
        return "success".equals(operationStatus);
    }

    public boolean isFailure() {
        return "failure".equals(operationStatus);
    }

    // 构建操作内容的便捷方法
    public static String buildOperationContent(String module, String action, String target) {
        return String.format("%s%s%s",
                getModuleDisplayName(module),
                getActionDisplayName(action),
                target);
    }

    private static String getModuleDisplayName(String module) {
        switch (module) {
            case "user": return "用户管理-";
            case "system": return "系统管理-";
            case "auth": return "认证授权-";
            default: return module + "-";
        }
    }

    private static String getActionDisplayName(String action) {
        switch (action) {
            case "create": return "创建";
            case "update": return "更新";
            case "delete": return "删除";
            case "export": return "导出";
            case "import": return "导入";
            default: return action;
        }
    }

    /**
     * 快速创建登录审计日志 - 使用实际字段
     */
    public static SystemOperationLogs createLoginLog(String account, String clientIp,
                                                     String userAgent, boolean success,
                                                     String errorMessage) {
        SystemOperationLogs log = new SystemOperationLogs();
        log.setTraceId(generateTraceId());
        log.setModule("auth");
        log.setAction("login");
        log.setOperationContent(buildOperationContent("auth", "login", "用户登录"));
        log.setOperatorAccount(account);
        log.setOperatorIp(clientIp);
        log.setBrowserInfo(userAgent);
        log.setOperationStatus(success ? "success" : "failure");
        log.setErrorMessage(errorMessage);
        log.setOperationSource("web");
        log.setSecurityLevel(success ? "medium" : "high");
        log.setDeviceType(detectDeviceType(userAgent));
        return log;
    }

    /**
     * 快速创建登出审计日志
     */
    public static SystemOperationLogs createLogoutLog(SystemUserAccounts user, String clientIp,
                                                      String userAgent) {
        SystemOperationLogs log = new SystemOperationLogs();
        log.setTraceId(generateTraceId());
        log.setModule("auth");
        log.setAction("logout");
        log.setOperationContent(buildOperationContent("auth", "logout", "用户登出"));
        log.setOperatorId(user.getId());
        log.setOperatorAccount(user.getAccount());
        log.setOperatorName(user.getName());
        log.setOperatorIp(clientIp);
        log.setBrowserInfo(userAgent);
        log.setOperationStatus("success");
        log.setOperationSource("web");
        log.setSecurityLevel("medium");
        log.setDeviceType(detectDeviceType(userAgent));
        return log;
    }

    /**
     * 快速创建用户管理操作日志
     */
    public static SystemOperationLogs createUserOperationLog(String action, SystemUserAccounts operator,
                                                             SystemUserAccounts targetUser,
                                                             String changeBefore, String changeAfter,
                                                             String changeFields) {
        SystemOperationLogs log = new SystemOperationLogs();
        log.setTraceId(generateTraceId());
        log.setModule("user");
        log.setAction(action);
        log.setOperationContent(buildOperationContent("user", action,
                targetUser != null ? targetUser.getName() : "用户"));
        log.setOperatorId(operator.getId());
        log.setOperatorAccount(operator.getAccount());
        log.setOperatorName(operator.getName());
        log.setTargetId(targetUser != null ? String.valueOf(targetUser.getId()) : null);
        log.setTargetType("user");
        log.setTargetContent(targetUser != null ? targetUser.getAccount() : null);
        log.setChangeBefore(changeBefore);
        log.setChangeAfter(changeAfter);
        log.setChangeFields(changeFields);
        log.setOperationStatus("success");
        log.setOperationSource("web");
        log.setSecurityLevel("high");
        return log;
    }

    /**
     * 快速创建数据变更审计日志
     */
    public static SystemOperationLogs createDataChangeLog(String module, String action,
                                                          SystemUserAccounts operator, Object targetId,
                                                          String targetType, String targetName,
                                                          String changeBefore, String changeAfter,
                                                          String changeFields) {
        SystemOperationLogs log = new SystemOperationLogs();
        log.setTraceId(generateTraceId());
        log.setModule(module);
        log.setAction(action);
        log.setOperationContent(buildOperationContent(module, action, targetName));
        log.setOperatorId(operator.getId());
        log.setOperatorAccount(operator.getAccount());
        log.setOperatorName(operator.getName());
        log.setTargetId(targetId != null ? String.valueOf(targetId) : null);
        log.setTargetType(targetType);
        log.setTargetContent(targetName);
        log.setChangeBefore(changeBefore);
        log.setChangeAfter(changeAfter);
        log.setChangeFields(changeFields);
        log.setOperationStatus("success");
        log.setOperationSource("web");
        log.setSecurityLevel("high");
        return log;
    }

    /**
     * 快速创建失败操作日志
     */
    public static SystemOperationLogs createFailedOperationLog(String module, String action,
                                                               SystemUserAccounts operator,
                                                               String errorMessage) {
        SystemOperationLogs log = new SystemOperationLogs();
        log.setTraceId(generateTraceId());
        log.setModule(module);
        log.setAction(action);
        log.setOperationContent(buildOperationContent(module, action, "操作失败"));
        if (operator != null) {
            log.setOperatorId(operator.getId());
            log.setOperatorAccount(operator.getAccount());
            log.setOperatorName(operator.getName());
        }
        log.setOperationStatus("failure");
        log.setErrorMessage(errorMessage);
        log.setOperationSource("web");
        log.setSecurityLevel("high");
        return log;
    }

    // 辅助方法
    private static String generateTraceId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    private static String detectDeviceType(String userAgent) {
        if (userAgent == null) return "desktop";
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("mobile")) return "mobile";
        if (userAgent.contains("tablet")) return "tablet";
        return "desktop";
    }
}