package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 用户角色详情响应DTO
 */
@Data
public class UserRoleDetailResponse {
    private String userAccount;
    private String userName;
    private List<UserRoleResponse> assignedRoles;
    private List<RoleResponse> availableRoles; // 可分配的角色列表
    private String defaultRoleCode;
}