package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色创建请求DTO
 */
@Data
public class CreateRoleRequest {

    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    private String roleName;

    @Size(max = 20, message = "角色类型长度不能超过20个字符")
    private String roleType;

    @Size(max = 20, message = "状态长度不能超过20个字符")
    private String status = "active";

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}