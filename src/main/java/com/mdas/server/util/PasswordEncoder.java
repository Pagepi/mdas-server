package com.mdas.server.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密工具类
 */
@Component
public class PasswordEncoder {

    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "SHA-256";

    /**
     * 生成加密密码
     */
    public String encode(String rawPassword) {
        try {
            // 生成随机盐值
            byte[] salt = generateSalt();

            // 计算加盐哈希
            byte[] hashedPassword = hashWithSalt(rawPassword, salt);

            // 组合盐值和哈希值: salt + hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 生成随机盐值
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * 计算加盐哈希
     */
    private byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        digest.update(salt);
        return digest.digest(password.getBytes());
    }

    /**
     * 验证密码
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            // 如果存储的密码不是加密格式，直接比较
            if (!isEncoded(encodedPassword)) {
                return rawPassword.equals(encodedPassword);
            }

            // 解码Base64
            byte[] combined = Base64.getDecoder().decode(encodedPassword);

            // 提取盐值和哈希值
            byte[] salt = new byte[SALT_LENGTH];
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);

            // 计算输入密码的哈希值
            byte[] inputHash = hashWithSalt(rawPassword, salt);

            // 比较哈希值
            return MessageDigest.isEqual(storedHash, inputHash);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查密码是否是加密格式
     */
    public boolean isEncoded(String password) {
        if (password == null || password.length() < 50) {
            return false;
        }
        try {
            Base64.getDecoder().decode(password);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}