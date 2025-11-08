package com.mdas.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色菜单权限表
 */
@Entity
@Table(name = "system_role_menus", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_code", "menu_code"})
})
@Data
public class SystemRoleMenus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "menu_code", nullable = false, length = 50)
    private String menuCode;

    @Column(name = "permission_type", nullable = false, length = 20)
    private String permissionType;

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