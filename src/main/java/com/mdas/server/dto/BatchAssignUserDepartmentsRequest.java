package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * 批量用户部门分配请求DTO
 */
@Data
public class BatchAssignUserDepartmentsRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    private List<String> departmentCodes;

    private String primaryDepartmentCode; // 主部门编码

    private String status = "active";
}