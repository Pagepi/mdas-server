package com.mdas.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统角色表
 */
@Entity
@Table(name = "system_roles")
@Data
public class SystemRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_code", nullable = false, length = 50, unique = true)
    private String roleCode;

    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;

    @Column(name = "role_type", length = 20)
    private String roleType;

    @Column(name = "status", length = 20)
    private String status = "active";

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_by_account", length = 50)
    private String createdByAccount;

    @Column(name = "created_by_name", length = 50)
    private String createdByName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_by_account", length = 50)
    private String updatedByAccount;

    @Column(name = "updated_by_name", length = 50)
    private String updatedByName;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "active";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}