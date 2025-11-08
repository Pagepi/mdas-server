package com.mdas.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 系统用户账户表
 * @TableName system_user_accounts
 */
@Entity
@Table(name = "system_user_accounts")
@Data
public class SystemUserAccounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account", unique = true, nullable = false, length = 50)
    private String account;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "wechat", length = 50)
    private String wechat;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "active"; // active, inactive, locked

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_by_account", length = 50)
    private String createdByAccount;

    @Column(name = "created_by_name", length = 100)
    private String createdByName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_by_account", length = 50)
    private String updatedByAccount;

    @Column(name = "updated_by_name", length = 100)
    private String updatedByName;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "role", length = 50)
    private String role = "user"; // user, admin, operator

    // 新增字段
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "login_fail_count")
    private Integer loginFailCount = 0;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        // 设置默认值
        if (status == null) {
            status = "active";
        }
        if (role == null) {
            role = "user";
        }
        if (loginFailCount == null) {
            loginFailCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SystemUserAccounts other = (SystemUserAccounts) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
                && (this.getWechat() == null ? other.getWechat() == null : this.getWechat().equals(other.getWechat()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getCreatedBy() == null ? other.getCreatedBy() == null : this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getCreatedByAccount() == null ? other.getCreatedByAccount() == null : this.getCreatedByAccount().equals(other.getCreatedByAccount()))
                && (this.getCreatedByName() == null ? other.getCreatedByName() == null : this.getCreatedByName().equals(other.getCreatedByName()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()))
                && (this.getUpdatedByAccount() == null ? other.getUpdatedByAccount() == null : this.getUpdatedByAccount().equals(other.getUpdatedByAccount()))
                && (this.getUpdatedByName() == null ? other.getUpdatedByName() == null : this.getUpdatedByName().equals(other.getUpdatedByName()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
                && (this.getLastLoginTime() == null ? other.getLastLoginTime() == null : this.getLastLoginTime().equals(other.getLastLoginTime()))
                && (this.getLoginFailCount() == null ? other.getLoginFailCount() == null : this.getLoginFailCount().equals(other.getLoginFailCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getWechat() == null) ? 0 : getWechat().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getCreatedByAccount() == null) ? 0 : getCreatedByAccount().hashCode());
        result = prime * result + ((getCreatedByName() == null) ? 0 : getCreatedByName().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedBy() == null) ? 0 : getUpdatedBy().hashCode());
        result = prime * result + ((getUpdatedByAccount() == null) ? 0 : getUpdatedByAccount().hashCode());
        result = prime * result + ((getUpdatedByName() == null) ? 0 : getUpdatedByName().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getLastLoginTime() == null) ? 0 : getLastLoginTime().hashCode());
        result = prime * result + ((getLoginFailCount() == null) ? 0 : getLoginFailCount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", account=").append(account);
        sb.append(", name=").append(name);
        sb.append(", password=").append("***"); // 密码不显示在toString中
        sb.append(", mobile=").append(mobile);
        sb.append(", wechat=").append(wechat);
        sb.append(", email=").append(email);
        sb.append(", status=").append(status);
        sb.append(", description=").append(description);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdByAccount=").append(createdByAccount);
        sb.append(", createdByName=").append(createdByName);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedByAccount=").append(updatedByAccount);
        sb.append(", updatedByName=").append(updatedByName);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", role=").append(role);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", loginFailCount=").append(loginFailCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    // 业务方法 - 检查用户是否可用
    public boolean isActive() {
        return "active".equals(status);
    }

    // 业务方法 - 检查是否是管理员
    public boolean isAdmin() {
        return "admin".equals(role);
    }

    // 业务方法 - 增加登录失败次数
    public void incrementLoginFailCount() {
        if (loginFailCount == null) {
            loginFailCount = 0;
        }
        loginFailCount++;
    }

    // 业务方法 - 重置登录失败次数
    public void resetLoginFailCount() {
        loginFailCount = 0;
    }

    // 业务方法 - 检查是否因登录失败被锁定
    public boolean isLockedByFailCount(int maxFailCount) {
        return loginFailCount != null && loginFailCount >= maxFailCount;
    }
}