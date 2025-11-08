package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单响应DTO（包含子菜单）
 */
@Data
public class MenuResponse {
    private Integer id;
    private String menuCode;
    private String menuName;
    private String menuIcon;
    private String menuPath;
    private String componentName;
    private String parentMenuCode;
    private Integer menuLevel;
    private Integer sortOrder;
    private String menuType;
    private String applicationType;
    private Boolean isExternal;
    private String externalUrl;
    private String permissionCode;
    private Boolean isVisible;
    private Boolean isEnabled;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;

    // 子菜单列表
    private List<MenuResponse> children;
}