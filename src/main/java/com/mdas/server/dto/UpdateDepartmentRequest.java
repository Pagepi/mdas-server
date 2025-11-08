package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 部门更新请求DTO
 */
@Data
public class UpdateDepartmentRequest {

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String departmentName;

    @Size(max = 50, message = "父部门编码长度不能超过50个字符")
    private String parentDepartmentCode;

    private Integer level;

    @Size(max = 50, message = "部门类型长度不能超过50个字符")
    private String departmentType;

    private Integer sortOrder;

    @Size(max = 500, message = "完整路径长度不能超过500个字符")
    private String fullPath;

    @Size(max = 50, message = "管理员用户ID长度不能超过50个字符")
    private String managerUserId;

    @Size(max = 50, message = "管理员账号长度不能超过50个字符")
    private String managerAccount;

    @Size(max = 100, message = "管理员姓名长度不能超过100个字符")
    private String managerName;

    @Size(max = 20, message = "状态长度不能超过20个字符")
    private String status;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}