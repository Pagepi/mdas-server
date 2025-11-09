package com.mdas.server.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncoder {

    private static final int SALT_LENGTH = 16;
    private static final String DELIMITER = ":";

    public String encode(CharSequence rawPassword) {
        try {
            // 生成随机盐值
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // 使用SHA-256进行哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hash = digest.digest(rawPassword.toString().getBytes());

            // 返回格式: base64(盐值):base64(哈希值)
            return Base64.getEncoder().encodeToString(salt) + DELIMITER +
                    Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            // 分割盐值和哈希值
            String[] parts = encodedPassword.split(DELIMITER);
            if (parts.length != 2) {
                return false; // 格式不正确
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // 使用相同的盐值计算输入密码的哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] actualHash = digest.digest(rawPassword.toString().getBytes());

            // 安全地比较哈希值
            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        }
    }
}