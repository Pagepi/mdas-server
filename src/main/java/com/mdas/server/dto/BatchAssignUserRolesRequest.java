package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 批量用户角色分配请求DTO
 */
@Data
public class BatchAssignUserRolesRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    private List<String> roleCodes;

    private String defaultRoleCode; // 默认角色编码

    private String status = "active";
}