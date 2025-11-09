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

        // 获取当前计数
        String countStr = (String) redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;

        if (count >= limit) {
            log.warn("速率限制触发: key={}, 计数={}, 限制={}", key, count, limit);
            throw new RateLimitException(rateLimit.message());
        }

        // 增加计数
        redisTemplate.opsForValue().increment(key);
        // 设置过期时间
        redisTemplate.expire(key, time, timeUnit);

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e;
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