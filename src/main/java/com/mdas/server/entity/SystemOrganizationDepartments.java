package com.mdas.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织架构表
 */
@Entity
@Table(name = "system_organization_departments")
@Data
public class SystemOrganizationDepartments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_code", nullable = false, length = 50, unique = true)
    private String departmentCode;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "parent_department_code", length = 50)
    private String parentDepartmentCode;

    @Column(name = "department_type", length = 50)
    private String departmentType;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "full_path", length = 500)
    private String fullPath;

    @Column(name = "manager_user_id", length = 50)
    private String managerUserId;

    @Column(name = "manager_account", length = 50)
    private String managerAccount;

    @Column(name = "manager_name", length = 100)
    private String managerName;

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
        if (level == null) {
            level = 1;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}