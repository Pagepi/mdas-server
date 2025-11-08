package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门响应DTO（包含子部门）
 */
@Data
public class DepartmentResponse {
    private Integer id;
    private String departmentCode;
    private String departmentName;
    private Integer level;
    private String parentDepartmentCode;
    private String departmentType;
    private Integer sortOrder;
    private String fullPath;
    private String managerUserId;
    private String managerAccount;
    private String managerName;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;

    // 子部门列表
    private List<DepartmentResponse> children;

    // 父部门名称（用于显示）
    private String parentDepartmentName;

    // 统计信息
    private Integer userCount;
}