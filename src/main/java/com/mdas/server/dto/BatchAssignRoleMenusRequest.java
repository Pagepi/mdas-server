package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * 批量角色菜单权限分配请求DTO
 */
@Data
public class BatchAssignRoleMenusRequest {

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private List<RoleMenuPermission> permissions;

    private String status = "active";
}