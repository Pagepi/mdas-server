package com.mdas.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统菜单表
 */
@Entity
@Table(name = "system_menus")
@Data
public class SystemMenus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_code", nullable = false, length = 50, unique = true)
    private String menuCode;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(name = "menu_icon", length = 100)
    private String menuIcon;

    @Column(name = "menu_path", length = 500)
    private String menuPath;

    @Column(name = "component_name", length = 200)
    private String componentName;

    @Column(name = "parent_menu_code", length = 50)
    private String parentMenuCode;

    @Column(name = "menu_level", nullable = false)
    private Integer menuLevel = 1;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "menu_type", nullable = false, length = 20)
    private String menuType;

    @Column(name = "application_type", length = 20)
    private String applicationType;

    @Column(name = "is_external", nullable = false)
    private Boolean isExternal = false;

    @Column(name = "external_url", length = 500)
    private String externalUrl;

    @Column(name = "permission_code", length = 100)
    private String permissionCode;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

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
        if (menuLevel == null) {
            menuLevel = 1;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
        if (isExternal == null) {
            isExternal = false;
        }
        if (isVisible == null) {
            isVisible = true;
        }
        if (isEnabled == null) {
            isEnabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}