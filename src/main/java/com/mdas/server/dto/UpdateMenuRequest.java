package com.mdas.server.dto;
// package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 菜单更新请求DTO
 */
@Data
public class UpdateMenuRequest {

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 100, message = "菜单名称长度不能超过100个字符")
    private String menuName;

    @Size(max = 100, message = "菜单图标长度不能超过100个字符")
    private String menuIcon;

    @Size(max = 500, message = "菜单路径长度不能超过500个字符")
    private String menuPath;

    @Size(max = 200, message = "组件名称长度不能超过200个字符")
    private String componentName;

    @Size(max = 50, message = "父菜单编码长度不能超过50个字符")
    private String parentMenuCode;

    private Integer sortOrder;

    @Size(max = 20, message = "菜单类型长度不能超过20个字符")
    private String menuType;

    @Size(max = 20, message = "应用类型长度不能超过20个字符")
    private String applicationType;

    private Boolean isExternal;

    @Size(max = 500, message = "外部URL长度不能超过500个字符")
    private String externalUrl;

    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    private String permissionCode;

    private Boolean isVisible;

    private Boolean isEnabled;

    @Size(max = 20, message = "状态长度不能超过20个字符")
    private String status;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}
