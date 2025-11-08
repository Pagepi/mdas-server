package com.mdas.server.service;

import com.mdas.server.entity.SystemLoginLogs;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.LoginLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录日志服务
 */
@Service
@Slf4j
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    /**
     * 记录登录成功日志
     */
    @Async
    public void logLoginSuccess(SystemUserAccounts user, String loginType, String clientIp,
                                String userAgent, String clientType, String traceId) {
        try {
            SystemLoginLogs loginLog = new SystemLoginLogs();
            loginLog.setTraceId(traceId);
            loginLog.setUserId(user.getId());
            loginLog.setUserAccount(user.getAccount());
            loginLog.setUserName(user.getName());
            loginLog.setLoginType(loginType);
            loginLog.setLoginStatus("success");
            loginLog.setClientIp(clientIp);
            loginLog.setUserAgent(userAgent);
            loginLog.setClientType(clientType);
            loginLog.setLoginTime(LocalDateTime.now());

            // 重置登录失败次数（如果之前有失败记录）
            user.setLoginFailCount(0);
            user.setLastLoginTime(LocalDateTime.now());

            loginLogRepository.save(loginLog);

            log.info("用户 {} 登录成功, IP: {}", user.getAccount(), clientIp);

        } catch (Exception e) {
            log.error("记录登录成功日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录登录失败日志
     */
    @Async
    public void logLoginFailure(String account, String loginType, String failureReason,
                                String clientIp, String userAgent, String traceId) {
        try {
            SystemLoginLogs loginLog = new SystemLoginLogs();
            loginLog.setTraceId(traceId);
            loginLog.setUserAccount(account);
            loginLog.setLoginType(loginType);
            loginLog.setLoginStatus("failure");
            loginLog.setFailureReason(failureReason);
            loginLog.setClientIp(clientIp);
            loginLog.setUserAgent(userAgent);
            loginLog.setLoginTime(LocalDateTime.now());

            loginLogRepository.save(loginLog);

            log.warn("登录失败: 账号 {}, 原因: {}, IP: {}", account, failureReason, clientIp);

        } catch (Exception e) {
            log.error("记录登录失败日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录账户锁定日志
     */
    @Async
    public void logAccountLocked(SystemUserAccounts user, String clientIp, String userAgent, String traceId) {
        try {
            SystemLoginLogs loginLog = new SystemLoginLogs();
            loginLog.setTraceId(traceId);
            loginLog.setUserId(user.getId());
            loginLog.setUserAccount(user.getAccount());
            loginLog.setUserName(user.getName());
            loginLog.setLoginType("password");
            loginLog.setLoginStatus("locked");
            loginLog.setFailureReason("account_locked");
            loginLog.setClientIp(clientIp);
            loginLog.setUserAgent(userAgent);
            loginLog.setLoginTime(LocalDateTime.now());

            loginLogRepository.save(loginLog);

            log.warn("账户被锁定: {}, IP: {}", user.getAccount(), clientIp);

        } catch (Exception e) {
            log.error("记录账户锁定日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录登出日志
     */
    @Async
    public void logLogout(SystemUserAccounts user, String clientIp, String userAgent, Long sessionDuration) {
        try {
            // 查找最近的登录记录并更新登出时间
            SystemLoginLogs lastLogin = loginLogRepository.findTopByUserIdAndLoginStatusOrderByLoginTimeDesc(
                    user.getId(), "success");

            if (lastLogin != null) {
                lastLogin.setLogoutTime(LocalDateTime.now());
                lastLogin.setSessionDuration(sessionDuration);
                loginLogRepository.save(lastLogin);

                log.info("用户 {} 登出, 会话时长: {}秒", user.getAccount(), sessionDuration);
            }

        } catch (Exception e) {
            log.error("记录登出日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 检查可疑登录
     */
    public boolean checkSuspiciousLogin(SystemUserAccounts user, String clientIp) {
        // 简单的可疑登录检测规则
        // 1. 检查最近1小时内是否有来自不同IP的登录
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long differentIpLogins = loginLogRepository.countByUserIdAndClientIpNotAndLoginTimeAfter(
                user.getId(), clientIp, oneHourAgo);

        if (differentIpLogins > 0) {
            log.warn("检测到可疑登录: 用户 {} 在1小时内有来自不同IP的登录", user.getAccount());
            return true;
        }

        // 2. 可以添加更多检测规则，如地理位置异常等

        return false;
    }

    /**
     * 获取用户最近登录次数
     */
    public long getRecentLoginFailCount(String account, int minutes) {
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(minutes);
        return loginLogRepository.countByUserAccountAndLoginStatusAndLoginTimeAfter(
                account, "failure", timeLimit);
    }
}