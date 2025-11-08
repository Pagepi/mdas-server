package com.mdas.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户部门关系表
 */
@Entity
@Table(name = "system_user_departments")
@Data
public class SystemUserDepartments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_account", nullable = false, length = 50)
    private String userAccount;

    @Column(name = "department_code", nullable = false, length = 50)
    private String departmentCode;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "position", length = 100)
    private String position;

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
        if (isPrimary == null) {
            isPrimary = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}