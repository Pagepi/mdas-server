package com.mdas.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色响应DTO
 */
@Data
public class RoleResponse {
    private Integer id;
    private String roleCode;
    private String roleName;
    private String roleType;
    private String status;
    private String description;
    private String createdByAccount;
    private String createdByName;
    private LocalDateTime createdAt;
    private String updatedByAccount;
    private String updatedByName;
    private LocalDateTime updatedAt;
}