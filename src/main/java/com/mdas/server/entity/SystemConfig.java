package com.mdas.server.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统配置表
 */
@Entity
@Table(name = "system_config")
@Data
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 配置键值
    @Column(name = "config_key", nullable = false, length = 100)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "config_type", nullable = false, length = 20)
    private String configType = "string"; // string, number, boolean, json, password

    // 配置描述
    @Column(name = "config_name", nullable = false, length = 200)
    private String configName;

    @Column(name = "config_description", columnDefinition = "TEXT")
    private String configDescription;

    // 配置范围
    @Column(name = "config_group", nullable = false, length = 50)
    private String configGroup = "system"; // system, security, jwt, login, api

    @Column(name = "config_category", nullable = false, length = 50)
    private String configCategory = "general"; // general, advanced, security

    // 配置约束
    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    // 排序和状态
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    // 操作信息
    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_by_account", length = 50)
    private String createdByAccount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_by_account", length = 50)
    private String updatedByAccount;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 版本控制
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (configType == null) {
            configType = "string";
        }
        if (configGroup == null) {
            configGroup = "system";
        }
        if (configCategory == null) {
            configCategory = "general";
        }
        if (isEncrypted == null) {
            isEncrypted = false;
        }
        if (isSystem == null) {
            isSystem = false;
        }
        if (isPublic == null) {
            isPublic = true;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
        if (isEnabled == null) {
            isEnabled = true;
        }
        if (version == null) {
            version = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 便捷方法
    public boolean isBooleanType() {
        return "boolean".equals(configType);
    }

    public boolean isNumberType() {
        return "number".equals(configType);
    }

    public boolean isJsonType() {
        return "json".equals(configType);
    }

    public boolean isPasswordType() {
        return "password".equals(configType);
    }

    // 获取转换后的值
    public Object getTypedValue() {
        if (!isEnabled) {
            return null;
        }

        try {
            switch (configType) {
                case "boolean":
                    return Boolean.parseBoolean(configValue);
                case "number":
                    if (configValue.contains(".")) {
                        return Double.parseDouble(configValue);
                    } else {
                        return Long.parseLong(configValue);
                    }
                case "json":
                    return configValue; // 实际使用时可以解析为JSON对象
                default:
                    return configValue;
            }
        } catch (Exception e) {
            return configValue; // 解析失败返回原始字符串
        }
    }

    // 设置值（自动转换为字符串）
    public void setTypedValue(Object value) {
        if (value == null) {
            this.configValue = "";
        } else {
            this.configValue = value.toString();
        }
    }

    // 检查配置是否可编辑
    public boolean isEditable() {
        return !isSystem;
    }

    // 获取配置组显示名称
    public String getConfigGroupDisplay() {
        switch (configGroup) {
            case "system": return "系统配置";
            case "security": return "安全配置";
            case "jwt": return "JWT配置";
            case "login": return "登录配置";
            case "api": return "API配置";
            default: return configGroup;
        }
    }
}