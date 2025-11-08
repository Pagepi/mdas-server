package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色菜单权限分配请求DTO
 */
@Data
public class AssignRoleMenuRequest {

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @NotBlank(message = "菜单编码不能为空")
    private String menuCode;

    @NotBlank(message = "权限类型不能为空")
    private String permissionType; // read, write, delete, etc.

    private String status = "active";

    private String description;
}