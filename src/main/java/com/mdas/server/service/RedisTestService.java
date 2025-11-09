package com.mdas.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisTestService implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("🔍 开始Redis连接测试...");

            // 测试Redis连接和基本操作
            String testKey = "mdas:test:connection";
            String testValue = "success_" + System.currentTimeMillis();

            // 写入测试数据
            redisTemplate.opsForValue().set(testKey, testValue);
            log.info("✅ Redis写入测试成功");

            // 读取测试数据
            String result = (String) redisTemplate.opsForValue().get(testKey);

            if (testValue.equals(result)) {
                log.info("✅ Redis读取测试成功");
                log.info("✅ Redis连接配置正常");

                // 清理测试数据
                redisTemplate.delete(testKey);
                log.info("✅ Redis清理测试数据成功");
            } else {
                log.error("❌ Redis读取测试失败: 期望值={}, 实际值={}", testValue, result);
            }

        } catch (Exception e) {
            log.error("❌ Redis连接测试失败: {}", e.getMessage());
            log.error("请检查: 1. Redis服务是否启动 2. 连接配置是否正确 3. 网络是否通畅");
        }
    }
}