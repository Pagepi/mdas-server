package com.mdas.server.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:mdas-default-secret-key-2024-change-in-production}")
    private String secret;

    @Value("${jwt.expiration:604800000}") // 默认7天
    private long expiration;

    @Value("${jwt.issuer:mdas-server}")
    private String issuer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成签名密钥（从配置读取）
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT token
     */
    public String generateToken(Integer userId, String account, String name, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("account", account);
        claims.put("name", name);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * 从token中获取用户账号
     */
    public String getAccountFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("account", String.class);
    }

    /**
     * 从token中获取用户姓名
     */
    public String getNameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("name", String.class);
    }

    /**
     * 从token中获取用户角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 将token加入黑名单
     */
    public void blacklistToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                String key = "jwt:blacklist:" + token;
                redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofMillis(ttl));
                log.info("Token已加入黑名单: {}", maskToken(token));
            }
        } catch (Exception e) {
            log.warn("将Token加入黑名单失败: {}", e.getMessage());
        }
    }

    /**
     * 检查token是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            String key = "jwt:blacklist:" + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.warn("检查Token黑名单失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 先检查是否在黑名单中
            if (isTokenBlacklisted(token)) {
                log.warn("Token已被加入黑名单: {}", maskToken(token));
                return false;
            }

            getAllClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", maskToken(token));
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Token格式错误: {}", maskToken(token));
            return false;
        } catch (SecurityException e) {
            log.warn("Token签名验证失败: {}", maskToken(token));
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Token参数错误: {}", maskToken(token));
            return false;
        } catch (Exception e) {
            log.warn("Token验证失败: {}", maskToken(token));
            return false;
        }
    }

    /**
     * 掩码token，只显示部分字符（安全考虑）
     */
    public String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

    /**
     * 获取token中的所有claims（带详细异常处理）
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", maskToken(token));
            throw new RuntimeException("Token已过期");
        } catch (MalformedJwtException e) {
            log.warn("Token格式错误: {}", maskToken(token));
            throw new RuntimeException("Token格式错误");
        } catch (SecurityException e) {
            log.warn("Token签名验证失败: {}", maskToken(token));
            throw new RuntimeException("Token签名验证失败");
        } catch (IllegalArgumentException e) {
            log.warn("Token参数错误: {}", maskToken(token));
            throw new RuntimeException("Token参数错误");
        } catch (Exception e) {
            log.warn("Token解析失败: {}", maskToken(token));
            throw new RuntimeException("Token验证失败");
        }
    }

    /**
     * 获取Token剩余有效时间（秒）- 新增方法
     */
    public long getRemainingSeconds(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return (expiration.getTime() - System.currentTimeMillis()) / 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取Token过期时间 - 新增方法
     */
    public Date getExpiration(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }
}