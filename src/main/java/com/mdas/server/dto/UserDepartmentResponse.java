package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户部门关系响应DTO
 */
@Data
public class UserDepartmentResponse {
    private Integer id;
    private String userAccount;
    private String userName;
    private String departmentCode;
    private String departmentName;
    private Boolean isPrimary;
    private String position;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;
}