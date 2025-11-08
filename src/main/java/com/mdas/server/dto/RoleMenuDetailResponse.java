package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 角色菜单权限详情响应DTO
 */
@Data
public class RoleMenuDetailResponse {
    private String roleCode;
    private String roleName;
    private List<RoleMenuResponse> assignedMenus;
    private List<MenuResponse> availableMenus; // 可分配的菜单列表
    private List<String> permissionTypes; // 可用的权限类型列表
}