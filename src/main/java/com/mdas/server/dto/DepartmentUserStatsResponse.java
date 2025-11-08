package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;

/**
 * 部门用户统计响应DTO
 */
@Data
public class DepartmentUserStatsResponse {
    private String departmentCode;
    private String departmentName;
    private Integer totalUsers;
    private Integer activeUsers;
    private Integer primaryUsers;

    // 可以添加一些计算字段
    public Double getActiveUserRatio() {
        if (totalUsers == 0) {
            return 0.0;
        }
        return (double) activeUsers / totalUsers * 100;
    }

    public Double getPrimaryUserRatio() {
        if (totalUsers == 0) {
            return 0.0;
        }
        return (double) primaryUsers / totalUsers * 100;
    }
}