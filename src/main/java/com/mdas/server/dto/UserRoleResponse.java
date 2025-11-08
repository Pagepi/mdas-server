package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户角色关系响应DTO
 */
@Data
public class UserRoleResponse {
    private Integer id;
    private String userAccount;
    private String roleCode;
    private String roleName;
    private Boolean isDefault;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;
}