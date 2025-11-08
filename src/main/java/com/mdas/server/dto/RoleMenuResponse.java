package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色菜单权限响应DTO
 */
@Data
public class RoleMenuResponse {
    private Integer id;
    private String roleCode;
    private String roleName;
    private String menuCode;
    private String menuName;
    private String permissionType;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;
}