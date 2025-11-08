package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 用户部门详情响应DTO
 */
@Data
public class UserDepartmentDetailResponse {
    private String userAccount;
    private String userName;
    private List<UserDepartmentResponse> assignedDepartments;
    private List<DepartmentResponse> availableDepartments; // 可分配的部门列表
    private String primaryDepartmentCode;
    private String primaryDepartmentName;
}