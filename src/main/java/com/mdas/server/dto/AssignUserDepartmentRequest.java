package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户部门分配请求DTO
 */
@Data
public class AssignUserDepartmentRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @NotBlank(message = "部门编码不能为空")
    private String departmentCode;

    private Boolean isPrimary = false;

    private String position;

    private String status = "active";

    private String description;
}