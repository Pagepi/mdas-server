package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * 用户部门批量操作请求DTO
 */
@Data
public class UserDepartmentsBatchRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    private List<String> addDepartmentCodes;  // 要添加的部门编码列表
    private List<String> removeDepartmentCodes; // 要移除的部门编码列表
    private String setPrimaryDepartmentCode; // 要设置的主部门编码
}