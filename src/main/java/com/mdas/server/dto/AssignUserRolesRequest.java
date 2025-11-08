package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户角色分配请求DTO
 */
@Data
public class AssignUserRolesRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private Boolean isDefault = false;

    private String status = "active";

    private String description;
}