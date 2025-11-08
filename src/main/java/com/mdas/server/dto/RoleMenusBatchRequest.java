package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * 角色菜单批量操作请求DTO
 */
@Data
public class RoleMenusBatchRequest {

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private List<RoleMenuPermission> addPermissions;  // 要添加的权限列表
    private List<String> removeMenuCodes; // 要移除的菜单编码列表
    private List<RoleMenuPermission> updatePermissions; // 要更新的权限列表
}