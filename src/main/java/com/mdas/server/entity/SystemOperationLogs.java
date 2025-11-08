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

    @PrePersist
    protected void onCreate() {
        if (operationTime == null) {
            operationTime = LocalDateTime.now();
        }
        if (operationStatus == null) {
            operationStatus = "success";
        }
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
}