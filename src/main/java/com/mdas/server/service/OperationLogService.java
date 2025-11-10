package com.mdas.server.service;

import com.mdas.server.entity.SystemOperationLogs;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.OperationLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 操作日志服务 - 记录业务操作详情
 */
@Service
@Slf4j
public class OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    /**
     * 异步记录操作日志
     */
    @Async
    public void logOperation(String module, String action, String operationContent,
                             SystemUserAccounts operator, String targetId, String targetType,
                             String changeBefore, String changeAfter, String changeFields,
                             String clientIp, String userAgent) {
        try {
            SystemOperationLogs rec = new SystemOperationLogs();
            rec.setTraceId(generateTraceId());
            rec.setModule(module);
            rec.setAction(action);
            rec.setOperationContent(operationContent);

            // 操作人信息
            if (operator != null) {
                rec.setOperatorId(operator.getId());
                rec.setOperatorAccount(operator.getAccount());
                rec.setOperatorName(operator.getName());
            }

            // 操作目标信息
            rec.setTargetId(targetId);
            rec.setTargetType(targetType);

            // 变更信息
            rec.setChangeBefore(changeBefore);
            rec.setChangeAfter(changeAfter);
            rec.setChangeFields(changeFields);

            // 客户端信息
            rec.setOperatorIp(clientIp);

            // 时间信息
            rec.setOperationTime(LocalDateTime.now());
            rec.setOperationStatus("success");

            operationLogRepository.save(rec);

            log.info("操作日志记录成功: {} {} by {}", module, action,
                    operator != null ? operator.getAccount() : "unknown");

        } catch (Exception e) {
            log.error("记录操作日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录登录尝试
     */
    public void logLoginAttempt(String account, String clientIp, String userAgent, boolean success, String errorMessage) {
        try {
            SystemOperationLogs logRec = SystemOperationLogs.createLoginLog(account, clientIp, userAgent, success, errorMessage);
            operationLogRepository.save(logRec);
            log.info("记录登录审计日志: 用户={}, 结果={}, IP={}", account, success ? "成功" : "失败", clientIp);
        } catch (Exception e) {
            log.error("记录登录审计日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录登出操作
     */
    public void logLogout(SystemUserAccounts user, String clientIp, String userAgent) {
        try {
            SystemOperationLogs logRec = SystemOperationLogs.createLogoutLog(user, clientIp, userAgent);
            operationLogRepository.save(logRec);
            log.info("记录登出审计日志: 用户={}, IP={}", user.getAccount(), clientIp);
        } catch (Exception e) {
            log.error("记录登出审计日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录用户管理操作
     */
    public void logUserOperation(String action, SystemUserAccounts operator,
                                 Integer targetUserId, String operationContent,
                                 String changeBefore, String changeAfter, String changeFields,
                                 String clientIp) {
        logOperation("user", action, operationContent, operator,
                targetUserId != null ? targetUserId.toString() : null,
                "user", changeBefore, changeAfter, changeFields, clientIp, null);
    }

    /**
     * 记录简单的操作日志（无变更详情）
     */
    public void logSimpleOperation(String module, String action, String operationContent,
                                   SystemUserAccounts operator, String clientIp) {
        logOperation(module, action, operationContent, operator,
                null, null, null, null, null, clientIp, null);
    }

    /**
     * 记录通用数据变更操作
     */
    public void logDataChange(String module, String action, SystemUserAccounts operator,
                              Object targetId, String targetType, String targetName,
                              String changeBefore, String changeAfter, String changeFields) {
        try {
            SystemOperationLogs logRec = SystemOperationLogs.createDataChangeLog(module, action, operator,
                    targetId, targetType, targetName, changeBefore, changeAfter, changeFields);
            operationLogRepository.save(logRec);
            log.info("记录数据变更审计日志: 模块={}, 操作={}, 操作人={}, 目标={}",
                    module, action, operator.getAccount(), targetName);
        } catch (Exception e) {
            log.error("记录数据变更审计日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录失败的操作
     */
    public void logFailedOperation(String module, String action, String operationContent,
                                   SystemUserAccounts operator, String errorMessage,
                                   String clientIp) {
        try {
            SystemOperationLogs rec = new SystemOperationLogs();
            rec.setTraceId(generateTraceId());
            rec.setModule(module);
            rec.setAction(action);
            rec.setOperationContent(operationContent);
            rec.setErrorMessage(errorMessage);
            rec.setOperationStatus("failure");

            if (operator != null) {
                rec.setOperatorId(operator.getId());
                rec.setOperatorAccount(operator.getAccount());
                rec.setOperatorName(operator.getName());
            }

            rec.setOperatorIp(clientIp);
            rec.setOperationTime(LocalDateTime.now());

            operationLogRepository.save(rec);

            log.warn("操作失败日志记录: {} {} by {} - {}", module, action,
                    operator != null ? operator.getAccount() : "unknown", errorMessage);

        } catch (Exception e) {
            log.error("记录失败操作日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录失败操作
     */
    public void logFailedOperation(String module, String action, SystemUserAccounts operator, String errorMessage) {
        try {
            SystemOperationLogs logRec = SystemOperationLogs.createFailedOperationLog(module, action, operator, errorMessage);
            operationLogRepository.save(logRec);
            log.warn("记录失败操作审计日志: 模块={}, 操作={}, 操作人={}, 错误={}",
                    module, action, operator != null ? operator.getAccount() : "未知", errorMessage);
        } catch (Exception e) {
            log.error("记录失败操作审计日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录权限变更操作
     */
    public void logPermissionChange(String action, SystemUserAccounts operator, SystemUserAccounts targetUser,
                                    String oldRole, String newRole) {
        try {
            String changeBefore = String.format("{\"role\": \"%s\"}", oldRole);
            String changeAfter = String.format("{\"role\": \"%s\"}", newRole);
            String changeFields = "role";

            SystemOperationLogs logRec = SystemOperationLogs.createDataChangeLog("auth", action, operator,
                    targetUser.getId(), "user", targetUser.getAccount(), changeBefore, changeAfter, changeFields);
            operationLogRepository.save(logRec);
            log.info("记录权限变更审计日志: 操作人={}, 目标用户={}, 角色变更: {} -> {}",
                    operator.getAccount(), targetUser.getAccount(), oldRole, newRole);
        } catch (Exception e) {
            log.error("记录权限变更审计日志失败: {}", e.getMessage());
        }
    }

    /**
     * 获取客户端IP
     */
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取用户代理信息
     */
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 生成追踪ID
     */
    private String generateTraceId() {
        return "OP_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}