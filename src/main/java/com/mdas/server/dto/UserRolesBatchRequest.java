package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * 用户角色批量操作请求DTO
 */
@Data
public class UserRolesBatchRequest {

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    private List<String> addRoleCodes;  // 要添加的角色编码列表
    private List<String> removeRoleCodes; // 要移除的角色编码列表
    private String setDefaultRoleCode; // 要设置的默认角色编码
}