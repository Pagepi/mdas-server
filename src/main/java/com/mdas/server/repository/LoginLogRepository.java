package com.mdas.server.repository;

import com.mdas.server.entity.SystemLoginLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<SystemLoginLogs, Long>,
        JpaSpecificationExecutor<SystemLoginLogs> {

    // 查找用户最近的成功登录记录
    SystemLoginLogs findTopByUserIdAndLoginStatusOrderByLoginTimeDesc(Integer userId, String loginStatus);

    // 统计指定时间段内的失败登录次数
    long countByUserAccountAndLoginStatusAndLoginTimeAfter(String userAccount, String loginStatus, LocalDateTime loginTime);

    // 统计用户来自不同IP的登录次数
    long countByUserIdAndClientIpNotAndLoginTimeAfter(Integer userId, String clientIp, LocalDateTime loginTime);

    // 查找用户的登录历史
    List<SystemLoginLogs> findByUserIdOrderByLoginTimeDesc(Integer userId);

    // 查找可疑登录（短时间内多次失败）
    @Query("SELECT ll FROM SystemLoginLogs ll WHERE ll.loginStatus = 'failure' AND ll.loginTime > :timeLimit " +
            "GROUP BY ll.userAccount HAVING COUNT(ll) >= :maxFailCount")
    List<SystemLoginLogs> findSuspiciousFailedLogins(@Param("timeLimit") LocalDateTime timeLimit,
                                                     @Param("maxFailCount") int maxFailCount);
}