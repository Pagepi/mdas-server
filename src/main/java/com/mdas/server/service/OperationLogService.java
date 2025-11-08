package com.mdas.server.service;

import com.mdas.server.entity.SystemOperationLogs;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.OperationLogRepository;
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
     * 生成追踪ID
     */
    private String generateTraceId() {
        return "OP_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}