package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色菜单权限项（用于批量操作）
 */
@Data
public class RoleMenuPermission {

    @NotBlank(message = "菜单编码不能为空")
    private String menuCode;

    @NotBlank(message = "权限类型不能为空")
    private String permissionType;

    // 可选：可以添加其他字段
    private String description;
}
