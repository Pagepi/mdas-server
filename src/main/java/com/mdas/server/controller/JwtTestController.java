package com.mdas.server.controller;

import com.mdas.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class JwtTestController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/jwt")
    public Map<String, Object> testJwt() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 生成Token
            String token = jwtUtil.generateToken(1, "admin", "管理员", "admin");
            result.put("token", jwtUtil.maskToken(token));
            result.put("fullToken", token);

            // 验证Token
            boolean isValid = jwtUtil.validateToken(token);
            result.put("isValid", isValid);

            // 解析用户信息
            result.put("userId", jwtUtil.getUserIdFromToken(token));
            result.put("account", jwtUtil.getAccountFromToken(token));
            result.put("name", jwtUtil.getNameFromToken(token));
            result.put("role", jwtUtil.getRoleFromToken(token));

            // 剩余时间
            result.put("remainingSeconds", jwtUtil.getRemainingSeconds(token));

            result.put("success", true);
            result.put("message", "JWT测试成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "JWT测试失败: " + e.getMessage());
        }

        return result;
    }
}