package com.mdas.server.aspect;

import com.mdas.server.annotation.RateLimit;
import com.mdas.server.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = generateKey(joinPoint, rateLimit);
        int limit = rateLimit.limit();
        int time = rateLimit.time();
        TimeUnit timeUnit = rateLimit.timeUnit();

        try {
            // 使用 increment 并正确处理返回类型
            Long count = redisTemplate.opsForValue().increment(key);

            // 如果是第一次设置，设置过期时间
            if (count != null && count == 1) {
                redisTemplate.expire(key, time, timeUnit);
            }

            if (count != null && count > limit) {
                log.warn("速率限制触发: key={}, 计数={}, 限制={}", key, count, limit);
                throw new RuntimeException(rateLimit.message());
            }

            return joinPoint.proceed();

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("速率限制检查失败", e);
        }
    }

    /**
     * 生成速率限制的Redis key
     */
    private String generateKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();

        // 获取请求IP
        String ipAddress = getClientIp();

        // 构建key: rate_limit:类名:方法名:IP
        return String.format("rate_limit:%s:%s:%s", className, methodName, ipAddress);
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        } catch (Exception e) {
            return "unknown";
        }
    }
}